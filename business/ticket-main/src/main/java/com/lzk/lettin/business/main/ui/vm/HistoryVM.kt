package com.lzk.lettin.business.main.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lzk.lettin.business.main.data.model.LotteryDraw
import com.lzk.lettin.business.main.data.model.LotteryType
import com.lzk.lettin.business.main.data.repository.LotteryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryVM
    @Inject
    constructor(
        private val repository: LotteryRepository,
    ) : ViewModel() {
        private val _state = MutableStateFlow(HistoryUiState(loading = true, draws = emptyList()))
        val state: StateFlow<HistoryUiState> = _state.asStateFlow()

        fun load(
            type: LotteryType,
            limit: Int = 50,
        ) {
            viewModelScope.launch {
                _state.value = HistoryUiState(loading = true, draws = emptyList())
                repository.observeLatest(type, limit).collect { list ->
                    _state.value = HistoryUiState(loading = false, draws = list)
                }
            }
        }

        fun refresh(type: LotteryType) {
            viewModelScope.launch {
                repository.refresh(type, 30)
            }
        }
    }

data class HistoryUiState(
    val loading: Boolean,
    val draws: List<LotteryDraw>,
)
