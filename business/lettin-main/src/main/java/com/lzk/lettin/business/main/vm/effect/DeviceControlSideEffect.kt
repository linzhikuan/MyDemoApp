package com.lzk.lettin.business.main.vm.effect

sealed interface DeviceControlSideEffect {
    data class ShowToast(val msg: String) : DeviceControlSideEffect
}