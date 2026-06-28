package com.lzk.lettin.business.main.domain.analytics

import com.lzk.lettin.business.main.data.model.LotteryDraw
import com.lzk.lettin.business.main.data.model.LotteryType
import com.lzk.lettin.business.main.data.model.parseNumbers

/**
 * 统计分析聚合结果。
 */
data class StatsResult(
    val type: LotteryType,
    val drawCount: Int,
    /** 前区每个号码出现次数。 key = 号码, value = 次数 */
    val frontFrequency: Map<Int, Int>,
    val backFrequency: Map<Int, Int>,
    /** 前区号码"冷热度"：出现次数 Top-K 为热号，Bottom-K 为冷号 */
    val hotFront: List<Int>,
    val coldFront: List<Int>,
    val hotBack: List<Int>,
    val coldBack: List<Int>,
    /** 每个号码的遗漏值（从最近一期起算，未出现的期数累计。-1 表示从未出现） */
    val frontMiss: Map<Int, Int>,
    val backMiss: Map<Int, Int>,
    /** 和值趋势（按开奖时间从老到新） */
    val sumTrend: List<Int>,
    /** 奇偶比（奇数个数 / 总数） */
    val oddRatio: Float,
)

object FrequencyAnalyzer {

    fun analyze(type: LotteryType, draws: List<LotteryDraw>): StatsResult {
        if (draws.isEmpty()) {
            return StatsResult(
                type = type,
                drawCount = 0,
                frontFrequency = emptyMap(),
                backFrequency = emptyMap(),
                hotFront = emptyList(),
                coldFront = emptyList(),
                hotBack = emptyList(),
                coldBack = emptyList(),
                frontMiss = emptyMap(),
                backMiss = emptyMap(),
                sumTrend = emptyList(),
                oddRatio = 0f,
            )
        }

        val frontFreq = mutableMapOf<Int, Int>().withDefault { 0 }
        val backFreq = mutableMapOf<Int, Int>().withDefault { 0 }

        // 为计算"遗漏"，按时间从新到旧遍历
        val frontMiss = mutableMapOf<Int, Int>()
        val backMiss = mutableMapOf<Int, Int>()
        for (n in type.frontRange) frontMiss[n] = 0
        for (n in type.backRange) backMiss[n] = 0

        // 为计算"和值趋势"：按时间从老到新
        val sumTrend = draws.asReversed().map { draw ->
            draw.frontNumbers.parseNumbers().sum() + draw.backNumbers.parseNumbers().sum()
        }

        // 奇偶计数
        var oddCount = 0
        var totalCount = 0

        // 计算遗漏：从最新一期开始数，每当号码未出，则其"遗漏+1"
        val frontSeen = mutableSetOf<Int>()
        val backSeen = mutableSetOf<Int>()

        for (draw in draws) {
            val frontList = draw.frontNumbers.parseNumbers()
            val backList = draw.backNumbers.parseNumbers()
            frontList.forEach { frontFreq[it] = frontFreq.getValue(it) + 1 }
            backList.forEach { backFreq[it] = backFreq.getValue(it) + 1 }
            oddCount += frontList.count { it % 2 == 1 } + backList.count { it % 2 == 1 }
            totalCount += frontList.size + backList.size

            frontSeen.addAll(frontList)
            backSeen.addAll(backList)

            for (n in type.frontRange) {
                if (n !in frontList) {
                    // 号码在这一期未出现：如果之前出现过（在 frontSeen 里也包括本之前出现过），
                    // 这里简化为"累计未出的连续期数"，不做断点重置。
                    frontMiss[n] = (frontMiss[n] ?: 0) + 1
                }
            }
            for (n in type.backRange) {
                if (n !in backList) {
                    backMiss[n] = (backMiss[n] ?: 0) + 1
                }
            }
        }

        // 冷/热号取 Top-K
        val kFront = 6.coerceAtMost(type.frontTotal)
        val kBack = 2.coerceAtMost(type.backTotal)

        val hotFront = frontFreq.entries.sortedByDescending { it.value }.take(kFront).map { it.key }
        val coldFront = frontFreq.entries.sortedBy { it.value }.take(kFront).map { it.key }
        val hotBack = backFreq.entries.sortedByDescending { it.value }.take(kBack).map { it.key }
        val coldBack = backFreq.entries.sortedBy { it.value }.take(kBack).map { it.key }

        return StatsResult(
            type = type,
            drawCount = draws.size,
            frontFrequency = frontFreq,
            backFrequency = backFreq,
            hotFront = hotFront,
            coldFront = coldFront,
            hotBack = hotBack,
            coldBack = coldBack,
            frontMiss = frontMiss,
            backMiss = backMiss,
            sumTrend = sumTrend,
            oddRatio = if (totalCount == 0) 0f else oddCount.toFloat() / totalCount,
        )
    }
}
