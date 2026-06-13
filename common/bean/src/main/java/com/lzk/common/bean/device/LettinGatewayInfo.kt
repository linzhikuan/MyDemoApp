package com.lzk.common.bean.device

data class LettinGatewayInfo(
    var mac: String,
    val ip: String,
    val port: Int,
    val name: String? = null,
)
