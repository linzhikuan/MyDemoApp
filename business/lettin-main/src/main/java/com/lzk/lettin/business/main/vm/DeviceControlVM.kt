package com.lzk.lettin.business.main.vm

import com.lzk.common.bean.device.LettinGatewayInfo
import com.lzk.common.servicce.device.getDeviceService
import com.lzk.core.log.logI
import com.lzk.lettin.business.main.vm.effect.DeviceControlSideEffect
import com.lzk.lettin.business.main.vm.event.DeviceControlEvent
import com.lzk.lettin.business.main.vm.state.DeviceControlUiState

class DeviceControlVM(
    private val gatewayInfo: LettinGatewayInfo,
) : BaseViewModel<DeviceControlUiState, DeviceControlEvent, DeviceControlSideEffect>() {
    companion object {
        private const val TAG = "DeviceControlVM"
    }

    override fun initialState(): DeviceControlUiState =
        DeviceControlUiState(
            gatewayInfo = gatewayInfo,
        )

    override suspend fun handleEvent(event: DeviceControlEvent) {
        logI(TAG, "handleEvent:$event")
        when (event) {
            is DeviceControlEvent.Connect -> {
                connect(event.ip, event.port)
            }
        }
    }

    private fun connect(
        ip: String,
        port: Int,
    ) {
        updateState {
            copy(isConnecting = true)
        }
        getDeviceService().connect(ip, port)
    }
}
