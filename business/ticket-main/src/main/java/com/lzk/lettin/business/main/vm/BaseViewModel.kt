package com.lzk.lettin.business.main.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<State, Event, SideEffect> : ViewModel() {
    private val _state: MutableStateFlow<State> by lazy {
        MutableStateFlow(initialState())
    }
    val state: StateFlow<State> = _state.asStateFlow()
    private val _sideEffect = Channel<SideEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    abstract fun initialState(): State

    fun onEvent(event: Event) {
        viewModelScope.launch {
            handleEvent(event)
        }
    }

    protected abstract suspend fun handleEvent(event: Event)

    protected fun updateState(update: State.() -> State) {
        _state.update(update)
    }

    protected suspend fun sendSideEffect(sideEffect: SideEffect) {
        _sideEffect.send(sideEffect)
    }
}
