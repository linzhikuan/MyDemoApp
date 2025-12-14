package com.lzk.lettin.business.main.vm.state

import com.lzk.common.bean.device.LettinGatewayInfo

data class HomeUiState(
    val gatewayList: List<LettinGatewayInfo>? = null,
    val isFindingHq: Boolean = false,
)
