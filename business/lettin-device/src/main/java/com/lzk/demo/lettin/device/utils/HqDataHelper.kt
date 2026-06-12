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
            var mac: String? = null
            val json = Utils.parasUdpJson(this.data)
            logD(TAG, "json:$json")
            val macBytes = ByteArray(8)
            System.arraycopy(this.data, 2, macBytes, 0, 8)
            mac = Utils.bytesToHexString(macBytes)
            val hqBean = GsonUtils.fromJson(json, HqBean::class.java)
            LettinGatewayInfo(name = hqBean?.getName(), mac = mac ?: hqBean?.mac)
        }.onFailure {
            logE(TAG, "parse udp data error: ${it.message}")
        }.getOrNull()

    private fun HqBean.getName(): String? =
        if (!this.obj?.name.isNullOrBlank()) {
            this.obj?.name
        } else {
            this.data?.name
        }
}
