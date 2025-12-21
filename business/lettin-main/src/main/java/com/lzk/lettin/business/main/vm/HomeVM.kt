package com.lzk.lettin.business.main.vm

import androidx.lifecycle.viewModelScope
import com.lzk.common.servicce.device.getDeviceService
import com.lzk.core.log.logD
import com.lzk.lettin.business.main.vm.effect.HomeUiSideEffect
import com.lzk.lettin.business.main.vm.event.HomeUiEvent
import com.lzk.lettin.business.main.vm.state.HomeUiState
import kotlinx.coroutines.launch

class HomeVM : BaseViewModel<HomeUiState, HomeUiEvent, HomeUiSideEffect>() {
    companion object {
        private const val TAG = "HomeVM"
    }

    override fun initialState(): HomeUiState = HomeUiState()

    override suspend fun handleEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.FindHq -> {
                updateGateway()
            }
        }
    }

    init {
        logD(TAG, "init")
        observeGateway()
        updateGateway()
    }

    private fun observeGateway() {
        viewModelScope.launch {
            getDeviceService().gatewayFlow.collect {
                logD(TAG, "gatewayList:$it")
                updateState {
                    copy(gatewayList = it, isFindingHq = false)
                }
            }
        }
    }

    private fun updateGateway() {
        logD(TAG, "updateGateway")
        updateState {
            copy(isFindingHq = true)
        }
        getDeviceService().syncGateway()
    }
}
