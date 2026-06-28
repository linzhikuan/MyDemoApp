package com.lzk.lettin.business.main.data.remote.api

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 彩票开奖历史数据的 Retrofit 接口。
 *
 * 不同聚合服务的路径、参数名差异较大，这里以常见约定作为默认：
 * - baseUrl 由 [LotteryDataModule] 提供（可从 BuildConfig 或外部配置中读取）
 * - 路径：`/api/lottery/history`
 * - 参数：type = "ssq" / "dlt", limit = 拉取条数
 *
 * 如果接入的实际服务路径不同，只需修改此处的 [GET] 与 [Query] 名称，
 * 或改用 `@Url` 动态传参；上/下游代码无需改动。
 */
interface LotteryApi {

    @GET("api/lottery/history")
    suspend fun getHistory(
        @Query("type") type: String,
        @Query("limit") limit: Int = 30,
    ): LotteryApiResponse<LotteryListData>?

    @GET("api/lottery/history")
    suspend fun getHistoryByCode(
        @Query("code") code: String,
        @Query("limit") limit: Int = 30,
    ): LotteryApiResponse<LotteryListData>?
}
