package com.lzk.lettin.business.main.vm.effect

sealed interface HomeUiSideEffect {
    data class ShowToast(
        val msg: String,
    ) : HomeUiSideEffect

    data class OpenSetting(
        val source: String,
    ) : HomeUiSideEffect
}
