package com.lzk.lettin.business.main.data.model

/**
 * 彩种枚举，每个彩种携带自己的规则元数据。
 * 所有算法（随机组合、频率统计等）都基于以下字段，
 * 从而以统一方式处理不同彩种。
 */
enum class LotteryType(
    val displayName: String,
    /** 前区(红球)选择个数 */
    val frontCount: Int,
    /** 前区号码范围 */
    val frontRange: IntRange,
    /** 后区(蓝球)选择个数 */
    val backCount: Int,
    /** 后区号码范围 */
    val backRange: IntRange,
) {
    /** 双色球 */
    SSQ(
        displayName = "双色球",
        frontCount = 6,
        frontRange = 1..33,
        backCount = 1,
        backRange = 1..16,
    ),

    /** 大乐透 */
    DLT(
        displayName = "大乐透",
        frontCount = 5,
        frontRange = 1..35,
        backCount = 2,
        backRange = 1..12,
    ),
    ;

    val frontTotal: Int get() = frontRange.last - frontRange.first + 1
    val backTotal: Int get() = backRange.last - backRange.first + 1

    companion object {
        fun from(raw: String?): LotteryType = values().firstOrNull { it.name == raw } ?: SSQ
    }
}
