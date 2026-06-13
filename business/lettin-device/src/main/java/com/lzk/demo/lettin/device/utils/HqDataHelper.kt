package com.lzk.demo.lettin.device.utils

import com.lzk.common.bean.device.HqBean
import com.lzk.common.bean.device.LettinGatewayInfo
import com.lzk.core.log.logD
import com.lzk.core.log.logE
import com.lzk.core.socket.bean.UdpInfo
import com.lzk.core.utils.GsonUtils
import com.lzk.core.utils.Utils

object HqDataHelper {
    private const val TAG = "HqDataHelper"

    fun UdpInfo.parserToLettin(): LettinGatewayInfo? =
        runCatching {
            val udpInfo = this
            val json = Utils.parasUdpJson(udpInfo.data)
            logD(TAG, "json:$json")
            GsonUtils.fromJson(json, HqBean::class.java)?.let {
                LettinGatewayInfo(
                    name = it.getName(),
                    mac = it.getMac(udpInfo),
                    ip = udpInfo.fromIp,
                    port = udpInfo.fromPort,
                )
            }
        }.onFailure {
            logE(TAG, "parse udp data error: ${it.message}")
        }.getOrNull()

    private fun HqBean.getName(): String? =
        if (!this.obj?.name.isNullOrBlank()) {
            this.obj?.name
        } else {
            this.data?.name
        }

    private fun HqBean.getMac(udpInfo: UdpInfo): String {
        var mac: String? = null
        val macBytes = ByteArray(8)
        System.arraycopy(udpInfo.data, 2, macBytes, 0, 8)
        mac = Utils.bytesToHexString(macBytes)
        return mac ?: this.mac ?: ""
    }
}
