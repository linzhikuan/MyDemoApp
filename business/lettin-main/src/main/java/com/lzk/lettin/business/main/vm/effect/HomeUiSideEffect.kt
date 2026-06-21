package com.lzk.lettin.business.main.vm.effect

import com.lzk.common.bean.device.LettinGatewayInfo

sealed interface HomeUiSideEffect {
    data class ShowToast(
        val msg: String,
    ) : HomeUiSideEffect

    data class OpenSetting(
        val gatewayList: List<LettinGatewayInfo>,
    )
}
