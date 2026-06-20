package com.lzk.lettin.business.main.vm.state

import com.lzk.common.bean.device.ConnectionState
import com.lzk.common.bean.device.LettinGatewayInfo
import com.lzk.demo.lettin.device.bean.AreaTableBean
import com.lzk.demo.lettin.device.bean.DeviceTableBean
import com.lzk.demo.lettin.device.bean.RoomTableBean

data class RoomWithAreas(
    val room: RoomTableBean,
    val areas: List<AreaWithDevices>,
    val deviceCount: Int,
)

data class AreaWithDevices(
    val area: AreaTableBean,
    val devices: List<DeviceTableBean>,
)

data class DeviceControlUiState(
    val gatewayInfo: LettinGatewayInfo? = null,
    val connectionState: ConnectionState = ConnectionState.Init,
    val isRefreshing: Boolean = false,
    val roomWithAreas: List<RoomWithAreas> = emptyList(),
)
