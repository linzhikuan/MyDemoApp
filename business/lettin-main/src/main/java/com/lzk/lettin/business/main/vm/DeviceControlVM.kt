package com.lzk.lettin.business.main.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.lzk.common.bean.device.ConnectionState
import com.lzk.common.bean.device.LettinGatewayInfo
import com.lzk.common.servicce.device.getDeviceService
import com.lzk.core.log.logI
import com.lzk.lettin.business.main.vm.effect.DeviceControlSideEffect
import com.lzk.lettin.business.main.vm.event.DeviceControlEvent
import com.lzk.lettin.business.main.vm.state.DeviceControlUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeviceControlVM
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        companion object {
            private const val TAG = "DeviceControlVM"
            private const val KEY_DATA = "data"
        }

        private val _state =
            MutableStateFlow(
                DeviceControlUiState(
                    gatewayInfo =
                        savedStateHandle.get<String>(KEY_DATA)?.let {
                            Gson().fromJson(it, LettinGatewayInfo::class.java)
                        },
                ),
            )
        val state: StateFlow<DeviceControlUiState> = _state.asStateFlow()

        private val _sideEffect = Channel<DeviceControlSideEffect>()
        val sideEffect = _sideEffect.receiveAsFlow()

        init {
            subscribeConnectionState()
        }

        fun onEvent(event: DeviceControlEvent) {
            viewModelScope.launch {
                handleEvent(event)
            }
        }

        private fun subscribeConnectionState() {
            viewModelScope.launch {
                getDeviceService().connectionStateFlow.collect { connectionState ->
                    logI(TAG, "connectionState changed: $connectionState")
                    _state.update { it.copy(connectionState = connectionState) }
                    when (connectionState) {
                        is ConnectionState.Connecting -> {
                            sendSideEffect(DeviceControlSideEffect.ShowToast("正在连接..."))
                        }

                        is ConnectionState.Connected -> {
                            sendSideEffect(DeviceControlSideEffect.ShowToast("连接成功"))
                        }

                        is ConnectionState.Error -> {
                            sendSideEffect(DeviceControlSideEffect.ShowToast("连接失败: ${connectionState.message}"))
                        }

                        else -> {}
                    }
                }
            }
        }

        private suspend fun sendSideEffect(effect: DeviceControlSideEffect) {
            _sideEffect.send(effect)
        }

        private fun handleEvent(event: DeviceControlEvent) {
            logI(TAG, "handleEvent:$event")
            when (event) {
                is DeviceControlEvent.Connect -> {
                    connect(event.ip, event.port)
                }

                is DeviceControlEvent.SyncGwTable -> syncGwTable(event.gwId, event.ip)
            }
        }

        private fun connect(
            ip: String,
            port: Int,
        ) {
            getDeviceService().connect(ip, port)
        }

        private fun syncGwTable(
            gwId: String,
            ip: String,
        ) {
            getDeviceService().syncGwTable(gwId, ip)
        }
    }
