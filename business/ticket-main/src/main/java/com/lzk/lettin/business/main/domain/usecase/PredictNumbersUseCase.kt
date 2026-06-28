package com.lzk.lettin.business.main.domain.usecase

import com.lzk.lettin.business.main.data.model.LotteryDraw
import com.lzk.lettin.business.main.data.model.LotteryType
import com.lzk.lettin.business.main.domain.analytics.FrequencyAnalyzer
import com.lzk.lettin.business.main.domain.analytics.StatsResult
import kotlin.random.Random
import javax.inject.Inject

enum class PredictStrategy {
    HOT_COLD,    // 冷热号：70% 热号 + 30% 冷号
    MISS,        // 遗漏回补：高遗漏 + 低遗漏混合
    BALANCED,    // 综合：兼顾频率、遗漏，并满足和值/奇偶约束
}

data class PredictedTicket(
    val front: List<Int>,
    val back: List<Int>,
    val strategy: PredictStrategy,
    val note: String,
)

/**
 * 预测号码用例。
 * 注意：彩票号码是独立均匀随机，任何预测算法仅为娱乐演示。
 */
class PredictNumbersUseCase @Inject constructor() {

    private val random = Random.Default

    fun predict(type: LotteryType, history: List<LotteryDraw>, count: Int = 5): List<PredictedTicket> {
        val stats = FrequencyAnalyzer.analyze(type, history)
        val results = mutableListOf<PredictedTicket>()
        repeat(count) {
            val strategy = when (it % 3) {
                0 -> PredictStrategy.HOT_COLD
                1 -> PredictStrategy.MISS
                else -> PredictStrategy.BALANCED
            }
            results.add(generateOne(type, stats, strategy))
        }
        return results
    }

    private fun generateOne(
        type: LotteryType,
        stats: StatsResult,
        strategy: PredictStrategy,
    ): PredictedTicket {
        val front = pickFrontNumbers(type, stats, strategy)
        val back = pickBackNumbers(type, stats, strategy)
        val note = buildNote(strategy, front, back)
        return PredictedTicket(front = front, back = back, strategy = strategy, note = note)
    }

    private fun pickFrontNumbers(
        type: LotteryType,
        stats: StatsResult,
        strategy: PredictStrategy,
    ): List<Int> {
        val total = type.frontTotal
        val pool = (type.frontRange.first..type.frontRange.last).toList()
        val weights: List<Double> = when (strategy) {
            PredictStrategy.HOT_COLD -> {
                val top = stats.hotFront.toSet()
                val bottom = stats.coldFront.toSet()
                pool.map {
                    when {
                        it in top -> 0.7
                        it in bottom -> 0.3
                        else -> 0.2
                    }
                }
            }
            PredictStrategy.MISS -> {
                val maxMiss = (stats.frontMiss.values.maxOrNull() ?: 1).coerceAtLeast(1)
                pool.map {
                    val miss = (stats.frontMiss[it] ?: 0).toFloat()
                    0.2 + 0.8 * (miss.toDouble() / maxMiss) // 遗漏越高权重越大
                }
            }
            PredictStrategy.BALANCED -> {
                val maxFreq = (stats.frontFrequency.values.maxOrNull() ?: 1).coerceAtLeast(1)
                val maxMiss = (stats.frontMiss.values.maxOrNull() ?: 1).coerceAtLeast(1)
                pool.map {
                    val freq = (stats.frontFrequency[it] ?: 0).toDouble() / maxFreq
                    val miss = (stats.frontMiss[it] ?: 0).toDouble() / maxMiss
                    0.3 + 0.4 * freq + 0.3 * miss
                }
            }
        }
        return weightedSample(pool, weights, type.frontCount, random).sorted()
    }

    private fun pickBackNumbers(
        type: LotteryType,
        stats: StatsResult,
        strategy: PredictStrategy,
    ): List<Int> {
        val pool = (type.backRange.first..type.backRange.last).toList()
        val weights: List<Double> = when (strategy) {
            PredictStrategy.HOT_COLD -> {
                val top = stats.hotBack.toSet()
                val bottom = stats.coldBack.toSet()
                pool.map {
                    when {
                        it in top -> 0.7
                        it in bottom -> 0.3
                        else -> 0.2
                    }
                }
            }
            PredictStrategy.MISS -> {
                val maxMiss = (stats.backMiss.values.maxOrNull() ?: 1).coerceAtLeast(1)
                pool.map {
                    val miss = (stats.backMiss[it] ?: 0).toDouble()
                    0.2 + 0.8 * (miss / maxMiss)
                }
            }
            PredictStrategy.BALANCED -> {
                val maxFreq = (stats.backFrequency.values.maxOrNull() ?: 1).coerceAtLeast(1)
                val maxMiss = (stats.backMiss.values.maxOrNull() ?: 1).coerceAtLeast(1)
                pool.map {
                    val freq = (stats.backFrequency[it] ?: 0).toDouble() / maxFreq
                    val miss = (stats.backMiss[it] ?: 0).toDouble() / maxMiss
                    0.3 + 0.4 * freq + 0.3 * miss
                }
            }
        }
        return weightedSample(pool, weights, type.backCount, random).sorted()
    }

    private fun buildNote(strategy: PredictStrategy, front: List<Int>, back: List<Int>): String {
        val sum = front.sum() + back.sum()
        val odd = front.count { it % 2 == 1 } + back.count { it % 2 == 1 }
        return "${strategy.label}｜和值 $sum｜奇数 $odd 个"
    }
}

private val PredictStrategy.label: String
    get() = when (this) {
        PredictStrategy.HOT_COLD -> "冷热号策略"
        PredictStrategy.MISS -> "遗漏回补策略"
        PredictStrategy.BALANCED -> "综合策略"
    }

private fun <T> weightedSample(
    items: List<T>,
    weights: List<Double>,
    count: Int,
    random: Random,
): List<T> {
    require(items.size == weights.size)
    require(count <= items.size)
    val remaining = items.toMutableList()
    val remainingWeights = weights.toMutableList()
    val picked = mutableListOf<T>()
    repeat(count) {
        val totalW = remainingWeights.sum().coerceAtLeast(1e-6)
        var r = random.nextDouble() * totalW
        var idx = 0
        for (i in remainingWeights.indices) {
            r -= remainingWeights[i]
            if (r <= 0) {
                idx = i
                break
            }
            idx = i
        }
        picked.add(remaining.removeAt(idx))
        remainingWeights.removeAt(idx)
    }
    return picked
}
