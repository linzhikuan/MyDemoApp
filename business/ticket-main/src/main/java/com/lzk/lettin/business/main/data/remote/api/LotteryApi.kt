package com.lzk.lettin.business.main.data.remote.api

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 接入 huiniao.top 的彩票开奖历史接口。
 *
 * 请求示例：
 *   GET https://api.huiniao.top/interface/home/lotteryHistory?type=ssq&page=1&limit=30
 *
 * 返回的 list 中每一期是按数字字段展开（one/two/three/...），
 * 由 [RealLotteryRemoteDataSource] 负责按【SSQ / DLT】规则拼装为 frontNumbers/backNumbers。
 */
interface LotteryApi {

    @GET("interface/home/lotteryHistory")
    suspend fun getHistory(
        @Query("type") type: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 30,
    ): LotteryApiResponse?
}
