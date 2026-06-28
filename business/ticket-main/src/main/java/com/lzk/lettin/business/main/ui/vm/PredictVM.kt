package com.lzk.lettin.business.main.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lzk.lettin.business.main.data.model.LotteryType
import com.lzk.lettin.business.main.data.model.SavedTicket
import com.lzk.lettin.business.main.data.model.toNumberString
import com.lzk.lettin.business.main.data.repository.LotteryRepository
import com.lzk.lettin.business.main.domain.usecase.PredictNumbersUseCase
import com.lzk.lettin.business.main.domain.usecase.PredictedTicket
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PredictVM
    @Inject
    constructor(
        private val repository: LotteryRepository,
        private val useCase: PredictNumbersUseCase,
    ) : ViewModel() {
        private val _state =
            MutableStateFlow(PredictUiState(loading = true, list = emptyList(), message = null))
        val state: StateFlow<PredictUiState> = _state.asStateFlow()

        fun load(type: LotteryType) {
            viewModelScope.launch {
                _state.value = PredictUiState(loading = true, emptyList(), null)
                val draws = repository.getLatest(type, 30)
                if (draws.isEmpty()) {
                    _state.value = PredictUiState(loading = false, emptyList(), "暂无历史数据，无法预测")
                    return@launch
                }
                val list = useCase.predict(type, draws, 5)
                _state.value = PredictUiState(loading = false, list, null)
            }
        }

        fun saveTicket(
            type: LotteryType,
            ticket: PredictedTicket,
        ) {
            viewModelScope.launch {
                repository.saveTicket(
                    SavedTicket(
                        type = type.name,
                        frontNumbers = ticket.front.toNumberString(),
                        backNumbers = ticket.back.toNumberString(),
                        source = "预测-${ticket.strategy.name}",
                        createdAt = System.currentTimeMillis(),
                    ),
                )
            }
        }
    }

data class PredictUiState(
    val loading: Boolean,
    val list: List<PredictedTicket>,
    val message: String? = null,
)
