package com.lzk.lettin.business.main.vm.state

import com.lzk.common.bean.device.ConnectionState
import com.lzk.common.bean.device.LettinGatewayInfo

data class DeviceControlUiState(
    val gatewayInfo: LettinGatewayInfo? = null,
    val connectionState: ConnectionState = ConnectionState.Init,
    val isRefreshing: Boolean = false,
)
