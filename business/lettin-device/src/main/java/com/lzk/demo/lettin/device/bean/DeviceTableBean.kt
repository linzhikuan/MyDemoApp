package com.lzk.demo.lettin.device.bean

data class DeviceTableBean(
    val deviceId: String,
    val roomId: Int,
    val areaId: Int,
    val portFeature: PortFeature?,
)

data class PortFeature(
    val onoff: OnOff?,
)

data class OnOff(
    val value: Int,
)
