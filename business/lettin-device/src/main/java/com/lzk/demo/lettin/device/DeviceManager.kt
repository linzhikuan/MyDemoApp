package com.lzk.demo.lettin.device

import com.lzk.core.socket.UdpClient
import com.lzk.core.socket.bean.UdpInfo
import com.lzk.core.utils.GsonUtils
import com.lzk.core.utils.Utils
import com.lzk.common.bean.device.LettinGatewayInfo
import com.lzk.common.bean.device.HqBean
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
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
        val instance: DeviceManager by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            DeviceManager()
        }
        const val UDP_LOCAL_PORT = 6000
        const val UDP_REMOTE_PORT = 7000
        const val BROADCAST_IP = "255.255.255.255"
    }

    private val scope = CoroutineScope(Job() + Dispatchers.IO)
    private val gatewayInfos = CopyOnWriteArrayList<LettinGatewayInfo>()

    private val udpClient: UdpClient by lazy {
        UdpClient()
    }

    private val _gatewayFlow =
        MutableSharedFlow<List<LettinGatewayInfo>>(
            replay = 1,
        )

    val gatewayFlow: SharedFlow<List<LettinGatewayInfo>>
        get() = _gatewayFlow.asSharedFlow()

    init {
        scope.launch {
            udpClient.dataFlow.collect {
                onUdpData(it)
            }
        }
    }

    fun syncGateway() {
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
            scope.launch {
                val encoder =
                    DataEncoder.hqDataEncode(params.toString().toByteArray(), cmd, tid).forEach {
                        udpClient.send(it, UDP_LOCAL_PORT, BROADCAST_IP, UDP_REMOTE_PORT)
                    }
                delay(2000)
            }
        }
    }

    private fun onUdpData(udpInfo: UdpInfo) {
        runCatching {
            var mac: String? = null
            val json = Utils.parasUdpJson(udpInfo.data)
            val macBytes = ByteArray(8)
            System.arraycopy(udpInfo.data, 2, macBytes, 0, 8)
            mac = Utils.bytesToHexString(macBytes)
            val hqBean = GsonUtils.fromJson(json, HqBean::class.java)
            val gatewayInfo =
                LettinGatewayInfo(name = hqBean?.obj?.name ?: "", mac = mac ?: hqBean?.mac)
            gatewayInfos.add(gatewayInfo)
        }.onFailure {
        }
    }
}
