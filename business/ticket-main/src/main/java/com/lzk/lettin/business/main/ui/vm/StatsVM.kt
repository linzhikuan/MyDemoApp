package com.lzk.lettin.business.main.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lzk.lettin.business.main.data.model.LotteryType
import com.lzk.lettin.business.main.data.repository.LotteryRepository
import com.lzk.lettin.business.main.domain.analytics.FrequencyAnalyzer
import com.lzk.lettin.business.main.domain.analytics.StatsResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatsVM @Inject constructor(
    private val repository: LotteryRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(StatsUiState(loading = true, null))
    val state: StateFlow<StatsUiState> = _state.asStateFlow()

    fun load(type: LotteryType, lookBack: Int = 30) {
        viewModelScope.launch {
            val draws = repository.getLatest(type, lookBack)
            val result = FrequencyAnalyzer.analyze(type, draws)
            _state.value = StatsUiState(loading = false, result = result)
        }
    }
}

data class StatsUiState(
    val loading: Boolean,
    val result: StatsResult?,
)
