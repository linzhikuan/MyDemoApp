package com.lzk.lettin.business.main.data.remote.api

import com.google.gson.annotations.SerializedName

/**
 * 彩票历史开奖接口返回的顶层 JSON 模型。
 * 兼容常见聚合服务的返回结构（如 code/data/list）。
 * 若实际 API 的字段命名不同，只需修改 @SerializedName 即可，
 * 业务层（LotteryDraw / LotteryType）完全不变。
 */
data class LotteryApiResponse<T>(
    @SerializedName("code") val code: Int = -1,
    @SerializedName("msg") val msg: String? = null,
    @SerializedName("data") val data: T? = null,
    @SerializedName("result") val result: T? = null,
)

/**
 * 一期开奖记录的 JSON 模型。
 * @see com.lzk.lettin.business.main.data.model.LotteryDraw
 */
data class LotteryDrawDto(
    @SerializedName("issueNo") val issueNo: String? = null,
    @SerializedName("issue") val issue: String? = null,
    @SerializedName("expect") val expect: String? = null,
    @SerializedName("date") val date: String? = null,
    @SerializedName("openDate") val openDate: String? = null,
    @SerializedName("time") val time: String? = null,
    @SerializedName("frontNumbers") val frontNumbers: String? = null,
    @SerializedName("red") val red: String? = null,
    @SerializedName("redBall") val redBall: String? = null,
    @SerializedName("backNumbers") val backNumbers: String? = null,
    @SerializedName("blue") val blue: String? = null,
    @SerializedName("blueBall") val blueBall: String? = null,
    @SerializedName("poolAmount") val poolAmount: Long? = null,
    @SerializedName("pool") val pool: Long? = null,
    @SerializedName("firstCount") val firstCount: Int? = null,
    @SerializedName("firstPrize") val firstPrize: Long? = null,
    @SerializedName("secondCount") val secondCount: Int? = null,
    @SerializedName("secondPrize") val secondPrize: Long? = null,
)

/**
 * 列表型返回值：data: { list: [...] }
 */
data class LotteryListData(
    @SerializedName("list") val list: List<LotteryDrawDto>? = null,
    @SerializedName("result") val result: List<LotteryDrawDto>? = null,
)
