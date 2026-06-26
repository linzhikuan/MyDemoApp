package com.lzk.lettin.business.main.vm

import com.lzk.core.log.logD
import com.lzk.lettin.business.main.vm.effect.HomeUiSideEffect
import com.lzk.lettin.business.main.vm.event.HomeUiEvent
import com.lzk.lettin.business.main.vm.state.HomeUiState

class HomeVM : BaseViewModel<HomeUiState, HomeUiEvent, HomeUiSideEffect>() {
    companion object {
        private const val TAG = "HomeVM"
    }

    override fun initialState(): HomeUiState = HomeUiState("")

    override suspend fun handleEvent(event: HomeUiEvent) {
    }

    init {
        logD(TAG, "init")
    }

}
