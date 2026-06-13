package com.lzk.lettin.business.main.vm.state

import com.lzk.common.bean.device.LettinGatewayInfo

data class DeviceControlUiState(
    val gatewayInfo: LettinGatewayInfo? = null,
    val isConnecting: Boolean = false,
)
