package com.lzk.demo.lettin.device

import com.lzk.common.bean.device.HqBean
import com.lzk.common.bean.device.LettinGatewayInfo
import com.lzk.core.log.logD
import com.lzk.core.log.logE
import com.lzk.core.socket.UdpClient
import com.lzk.core.socket.bean.UdpInfo
import com.lzk.core.utils.GsonUtils
import com.lzk.core.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
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
        const val BROADCAST_IP = "255.255.255.255"
    }

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val gatewayInfos = CopyOnWriteArrayList<LettinGatewayInfo>()

    private val udpClient: UdpClient by lazy {
        UdpClient.instance
    }

    private val _gatewayFlow =
        MutableSharedFlow<List<LettinGatewayInfo>>(
            replay = 1,
        )

    val gatewayFlow: SharedFlow<List<LettinGatewayInfo>>
        get() = _gatewayFlow.asSharedFlow()

    init {
        scope.launch {
            udpClient.udpDataFlow.collect {
                logD(TAG, "udpState:$it")
                onUdpData(it)
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
                            udpClient.sendMessage(data, BROADCAST_IP, UDP_REMOTE_PORT, UDP_LOCAL_PORT)
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

    private fun onUdpData(udpInfo: UdpInfo) {
        runCatching {
            var mac: String? = null
            val json = Utils.parasUdpJson(udpInfo.data)
            logD(TAG, "json:$json")
            val macBytes = ByteArray(8)
            System.arraycopy(udpInfo.data, 2, macBytes, 0, 8)
            mac = Utils.bytesToHexString(macBytes)
            val hqBean = GsonUtils.fromJson(json, HqBean::class.java)
            val gatewayInfo =
                LettinGatewayInfo(name = hqBean?.obj?.name ?: "", mac = mac ?: hqBean?.mac)
            gatewayInfos.add(gatewayInfo)
        }.onFailure {
            logE(TAG, "parse udp data error: ${it.message}")
        }
    }
}
