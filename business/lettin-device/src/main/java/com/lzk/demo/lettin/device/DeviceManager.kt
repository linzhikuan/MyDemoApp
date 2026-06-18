package com.lzk.demo.lettin.device

import com.lzk.common.bean.device.ConnectionState
import com.lzk.common.bean.device.LettinGatewayInfo
import com.lzk.common.servicce.http.getHttpService
import com.lzk.core.log.logD
import com.lzk.core.log.logE
import com.lzk.core.log.logI
import com.lzk.core.socket.TcpClient
import com.lzk.core.socket.UdpClient
import com.lzk.core.socket.bean.TcpState
import com.lzk.core.socket.bean.UdpInfo
import com.lzk.demo.lettin.device.inner.LettinAPI
import com.lzk.demo.lettin.device.utils.HqDataHelper.parserToLettin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.random.Random

class DeviceManager {
    companion object {
        private const val TAG = "DeviceManager"
        val instance: DeviceManager by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            DeviceManager()
        }
        const val UDP_LOCAL_PORT = 6000
        const val UDP_REMOTE_PORT = 7000

        const val LETTIN4_GW_T = 1 // 网关信息表
        const val LETTIN4_DEV_T = 2 // 设备表
        const val LETTIN4_ROOM_T = 3 // 房间表
        const val LETTIN4_AREA_T = 4 // 区域表
        const val LETTIN4_SNAPSHOT_T = 5 // 快照表
        const val LETTIN4_SCENE_T = 6 // 场景表
        const val LETTIN4_APPRES_T = 7 // 预留字段表

        const val CMD_TABLE_QUERY = 4004 // 查询表操作

        const val BROADCAST_IP = "255.255.255.255"
    }

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val gatewayInfos = CopyOnWriteArrayList<LettinGatewayInfo>()
    private var currentIp: String? = null
    private var currentPort: Int? = null

    private val udpClient: UdpClient by lazy {
        UdpClient.instance
    }

    private val tcpClient: TcpClient by lazy {
        TcpClient()
    }

    private val _gatewayFlow =
        MutableSharedFlow<List<LettinGatewayInfo>>(
            replay = 1,
        )

    val gatewayFlow: SharedFlow<List<LettinGatewayInfo>>
        get() = _gatewayFlow.asSharedFlow()

    private val _connectionStateFlow = MutableSharedFlow<ConnectionState>(replay = 1)
    val connectionStateFlow: SharedFlow<ConnectionState>
        get() = _connectionStateFlow.asSharedFlow()

    init {
        scope.launch {
            udpClient.udpDataFlow.collect {
                logD(TAG, "udpState:$it")
                onUdpData(it)
            }
        }
        scope.launch {
            tcpClient.state.collect { tcpState ->
                logD(TAG, "tcpState:$tcpState")
                val connectionState =
                    when (tcpState) {
                        is TcpState.Init -> ConnectionState.Init
                        is TcpState.Connecting -> ConnectionState.Connecting
                        is TcpState.ConnectSuccess ->
                            ConnectionState.Connected(
                                ip = currentIp ?: "",
                                port = currentPort ?: 0,
                            )

                        is TcpState.ConnectFailed ->
                            ConnectionState.Error(
                                tcpState.throwable.message ?: "连接失败",
                            )

                        is TcpState.OnClosed -> ConnectionState.Disconnected(tcpState.throwable?.message)
                        is TcpState.OnReceiveMsg -> {
                            logI(TAG, "onReceiveMsg")
                            null
                        }

                        is TcpState.OnReceiveMsgFailed ->
                            ConnectionState.Error(
                                tcpState.throwable.message ?: "接收消息失败",
                            )

                        is TcpState.OnSendMsgFailed ->
                            ConnectionState.Error(
                                tcpState.throwable.message ?: "发送消息失败",
                            )
                    }
                connectionState?.let { _connectionStateFlow.emit(it) }
            }
        }
    }

    fun syncGateway() {
        logD(TAG, "syncGateway")
        scope.launch {
            gatewayInfos.clear()
            val tid = Random.nextInt(32767)
            val cmd = 1
            runCatching {
                JSONObject().apply {
                    put("Tid", tid)
                    put("Cmd", cmd)
                    put("Token", "lettintesttokena")
                }
            }.onSuccess { params ->
                logD(TAG, "params:$params")
                DataEncoder
                    .hqDataEncode(params.toString().toByteArray(), cmd, tid)
                    .forEach { data ->
                        runCatching {
                            udpClient.sendMessage(
                                data,
                                BROADCAST_IP,
                                UDP_REMOTE_PORT,
                                UDP_LOCAL_PORT,
                            )
                        }.onFailure {
                            logE(TAG, "send udp error: ${it.message}")
                        }.onSuccess {
                            logD(TAG, "send udp success:${data.contentToString()}")
                        }
                    }
                delay(2000)
                logD(TAG, "gatewayInfos:${gatewayInfos.size}")
                _gatewayFlow.emit(gatewayInfos.toList())
            }.onFailure {
                logE(TAG, "send udp error: ${it.message}")
                _gatewayFlow.emit(emptyList())
            }
        }
    }

    fun connectDevice(
        ip: String,
        port: Int,
    ) {
        currentIp = ip
        currentPort = port
        scope.launch {
            tcpClient
                .connect(ip, port)
                .onSuccess {
                    logI(TAG, "connectDevice success ip:$ip, port:$port")
                }.onFailure {
                    logE(TAG, "connectDevice failed", it)
                }
        }
    }

    fun queryTable(
        gatewayId: String,
        ip: String,
    ) {
        scope.launch {
            val tid = Random.nextInt(32767)
            val cmd = CMD_TABLE_QUERY
            runCatching {
                JSONObject().apply {
                    put("Tid", tid)
                    put("Cmd", cmd)
                    put("gwId", gatewayId)
                    val jsonArray = JSONArray()
                    val obj1 = JSONObject()
                    obj1.put("tableId", LETTIN4_GW_T)
                    obj1.put("ver", 0)
                    jsonArray.put(obj1)
                    put("data", jsonArray)
                    put("Token", "lettintesttokena")
                }
            }.onSuccess { params ->
                logD(TAG, "params:$params")
                runCatching {
                    getHttpService().getService(LettinAPI::class.java, ip).request(
                        RequestBody.create(
                            "application/json; charset=utf-8".toMediaTypeOrNull(),
                            params.toString(),
                        ),
                    )
                }.onFailure {
                    logE(TAG, "query error", it)
                }

//                DataEncoder
//                    .hqDataEncode(params.toString().toByteArray(), cmd, tid)
//                    .forEach { data ->
//                        tcpClient
//                            .sendMessage(data)
//                            .onSuccess {
//                                logI(TAG, "send success")
//                            }.onFailure {
//                                logE(TAG, "send failed", it)
//                            }
//                    }
            }
        }
    }

    private fun onUdpData(udpInfo: UdpInfo) {
        udpInfo.parserToLettin()?.let { gatewayInfos.add(it) }
    }
}
