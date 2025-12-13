package com.lzk.lettin.business.vm

import androidx.lifecycle.viewModelScope
import com.lzk.common.servicce.device.getDeviceService
import com.lzk.core.log.logD
import com.lzk.lettin.business.vm.effect.HomeUiSideEffect
import com.lzk.lettin.business.vm.event.HomeUiEvent
import com.lzk.lettin.business.vm.state.HomeUiState
import kotlinx.coroutines.launch

class HomeVM : BaseViewModel<HomeUiState, HomeUiEvent, HomeUiSideEffect>() {
    override fun initialState(): HomeUiState = HomeUiState()

    override suspend fun handleEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.FindHq -> {
            }
        }
    }

    init {
        observeGateway()
        updateGateway()
    }

    private fun observeGateway() {
        viewModelScope.launch {
            getDeviceService().gatewayFlow.collect {
                logD("gatewayList:$it")
                updateState {
                    copy(gatewayList = it)
                }
            }
        }
    }

    private fun updateGateway() {
        logD("updateGateway")
        viewModelScope.launch {
            getDeviceService().syncGateway()
        }
    }
}
