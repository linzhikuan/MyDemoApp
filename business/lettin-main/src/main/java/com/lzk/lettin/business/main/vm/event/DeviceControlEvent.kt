package com.lzk.lettin.business.main.vm.event

sealed interface DeviceControlEvent {
    data class Connect(
        val ip: String,
        val port: Int,
    ) : DeviceControlEvent
}
