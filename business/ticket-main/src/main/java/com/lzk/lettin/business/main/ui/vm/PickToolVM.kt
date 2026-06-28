package com.lzk.lettin.business.main.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lzk.lettin.business.main.data.model.LotteryType
import com.lzk.lettin.business.main.data.model.SavedTicket
import com.lzk.lettin.business.main.data.model.toNumberString
import com.lzk.lettin.business.main.data.repository.LotteryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class PickToolVM @Inject constructor(
    private val repository: LotteryRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(
        PickUiState(
            type = LotteryType.SSQ,
            mode = PickMode.RANDOM,
            selectedFront = emptySet(),
            selectedBack = emptySet(),
            generated = null,
            message = null,
        ),
    )
    val state: StateFlow<PickUiState> = _state.asStateFlow()

    fun resetForType(type: LotteryType) {
        _state.value = _state.value.copy(
            type = type,
            selectedFront = emptySet(),
            selectedBack = emptySet(),
            generated = null,
            message = null,
        )
    }

    fun changeMode(mode: PickMode) {
        _state.value = _state.value.copy(mode = mode, generated = null, message = null)
    }

    fun toggleFront(num: Int) {
        val current = _state.value.selectedFront
        val next = if (num in current) current - num else current + num
        _state.value = _state.value.copy(selectedFront = next, generated = null)
    }

    fun toggleBack(num: Int) {
        val current = _state.value.selectedBack
        val next = if (num in current) current - num else current + num
        _state.value = _state.value.copy(selectedBack = next, generated = null)
    }

    fun generateRandom(n: Int = 3) {
        val type = _state.value.type
        val random = Random.Default
        val tickets = List(n) {
            GeneratedTicket(
                front = randomNumbers(type.frontRange, type.frontCount, random),
                back = randomNumbers(type.backRange, type.backCount, random),
            )
        }
        _state.value = _state.value.copy(generated = tickets, message = "已生成 $n 注（点击可保存）")
    }

    /**
     * 复式：从选定号码中随机组合出若干注
     */
    fun generateCompound(max: Int = 10) {
        val type = _state.value.type
        val front = _state.value.selectedFront.toList()
        val back = _state.value.selectedBack.toList()
        if (front.size < type.frontCount || back.size < type.backCount) {
            _state.value = _state.value.copy(
                message = "请至少选 ${type.frontCount} 个前区号与 ${type.backCount} 个后区号",
                generated = null,
            )
            return
        }
        val random = Random.Default
        val result = mutableListOf<GeneratedTicket>()
        val seen = mutableSetOf<String>()
        repeat(1000) {
            if (result.size >= max) return@repeat
            val f = randomNumbersFrom(front, type.frontCount, random).sorted()
            val b = randomNumbersFrom(back, type.backCount, random).sorted()
            val key = "${f.joinToString(",")}|${b.joinToString(",")}"
            if (key !in seen) {
                seen.add(key)
                result.add(GeneratedTicket(f, b))
            }
        }
        _state.value = _state.value.copy(
            generated = result,
            message = "已生成 ${result.size} 注（点击可保存）",
        )
    }

    /**
     * 自选模式：直接将已选号码当作 1 注
     */
    fun confirmManual() {
        val type = _state.value.type
        val front = _state.value.selectedFront.toList().sorted()
        val back = _state.value.selectedBack.toList().sorted()
        if (front.size != type.frontCount || back.size != type.backCount) {
            _state.value = _state.value.copy(
                message = "请精确选择 ${type.frontCount} 个前区 & ${type.backCount} 个后区",
                generated = null,
            )
            return
        }
        _state.value = _state.value.copy(
            generated = listOf(GeneratedTicket(front = front, back = back)),
            message = "已生成 1 注（点击可保存）",
        )
    }
    fun generateDanTuo() {
        val type = _state.value.type
        val dan = _state.value.selectedFront.toList()
        if (dan.size >= type.frontCount) {
            _state.value = _state.value.copy(
                message = "胆码数应少于 ${type.frontCount}。若已选定足够号码，可使用复式",
                generated = null,
            )
            return
        }
        val others = (type.frontRange).toList() - dan.toSet()
        val need = type.frontCount - dan.size
        if (others.size < need) {
            _state.value = _state.value.copy(message = "可用拖码不足", generated = null)
            return
        }
        val random = Random.Default
        val backPool = _state.value.selectedBack.toList().ifEmpty { (type.backRange).toList() }
        val tickets = List(5) {
            GeneratedTicket(
                front = (dan + randomNumbersFrom(others, need, random)).sorted(),
                back = randomNumbersFrom(backPool, type.backCount, random).sorted(),
            )
        }
        _state.value = _state.value.copy(
            generated = tickets,
            message = "胆码: ${dan.sorted().joinToString(",")}｜已生成 5 注拖码组合",
        )
    }

    fun saveTicket(ticket: GeneratedTicket) {
        viewModelScope.launch {
            repository.saveTicket(
                SavedTicket(
                    type = _state.value.type.name,
                    frontNumbers = ticket.front.toNumberString(),
                    backNumbers = ticket.back.toNumberString(),
                    source = "选号-${_state.value.mode.label}",
                    createdAt = System.currentTimeMillis(),
                ),
            )
            _state.value = _state.value.copy(message = "已保存 1 注")
        }
    }

    private fun randomNumbers(range: IntRange, count: Int, random: Random): List<Int> =
        range.toList().shuffled(random).take(count).sorted()

    private fun randomNumbersFrom(pool: List<Int>, count: Int, random: Random): List<Int> =
        pool.shuffled(random).take(count)
}

enum class PickMode(val label: String) {
    RANDOM("机选"),
    MANUAL("自选"),
    DANTUO("胆拖"),
    COMPOUND("复式"),
}

data class GeneratedTicket(
    val front: List<Int>,
    val back: List<Int>,
)

data class PickUiState(
    val type: LotteryType,
    val mode: PickMode,
    val selectedFront: Set<Int>,
    val selectedBack: Set<Int>,
    val generated: List<GeneratedTicket>?,
    val message: String?,
)
