package com.lzk.lettin.business.main.data.remote.api

import com.google.gson.annotations.SerializedName

/**
 * huiniao.top 彩票历史接口顶层 JSON。
 * 示例：
 * ```
 * {
 *   "code": 1,
 *   "info": "成功",
 *   "data": {
 *     "data": {
 *       "list": [ { "code":"24049", "day":"2024-02-28", "one":6, "two":3, ... } ],
 *       "totalCount": 6821,
 *       ...
 *     }
 *   }
 * }
 * ```
 */
data class LotteryApiResponse(
    @SerializedName("code") val code: Int = -1,
    @SerializedName("info") val info: String? = null,
    @SerializedName("data") val data: LotteryOuterData? = null,
)

data class LotteryOuterData(
    @SerializedName("data") val data: LotteryListData? = null,
)

data class LotteryListData(
    @SerializedName("list") val list: List<LotteryDrawDto>? = null,
)

/**
 * huiniao.top 一期开奖记录。
 * - SSQ：one..six 为红球，seven 为蓝球
 * - DLT：one..five 为前区，six / seven 为后区（后区共 2 个）
 * 其他彩种字段统一留空，不影响解析。
 */
data class LotteryDrawDto(
    @SerializedName("code") val issueNo: String? = null,
    @SerializedName("day") val date: String? = null,
    @SerializedName("open_time") val openTime: String? = null,

    @SerializedName("one")    val one: Int? = null,
    @SerializedName("two")    val two: Int? = null,
    @SerializedName("three")  val three: Int? = null,
    @SerializedName("four")   val four: Int? = null,
    @SerializedName("five")   val five: Int? = null,
    @SerializedName("six")    val six: Int? = null,
    @SerializedName("seven")  val seven: Int? = null,
    @SerializedName("eight")  val eight: Int? = null,

    @SerializedName("sales") val sales: Long? = null,
    @SerializedName("poolmoney") val poolMoney: Long? = null,
)
