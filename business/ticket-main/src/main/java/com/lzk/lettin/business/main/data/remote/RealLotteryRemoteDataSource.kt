package com.lzk.lettin.business.main.data.remote

import com.lzk.lettin.business.main.data.model.LotteryDraw
import com.lzk.lettin.business.main.data.model.LotteryType
import com.lzk.lettin.business.main.data.remote.api.LotteryApiResponse
import com.lzk.lettin.business.main.data.remote.api.LotteryListData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

/**
 * 真实 API 数据源。
 * 封装 [LotteryApi] 为统一的 [LotteryRemoteDataSource]。
 * - 使用传入的 `type` 与服务端的 "lottery code" 映射在 [toServerCode]。
 */
class RealLotteryRemoteDataSource(
    private val api: LotteryApi,
    private val ioDispatcher: CoroutineDispatcher,
) : LotteryRemoteDataSource {

    override suspend fun fetchLatest(
        type: LotteryType,
        count: Int,
    ): List<LotteryDraw> = withContext(ioDispatcher) {
        // 1) 调用服务端
        val response = api.getHistory(type = type.toServerCode(), limit = count)
        val payload = response?.data ?: response?.result
        val list = payload?.list ?: payload?.result.orEmpty()

        // 2) 转换为 LotteryDraw
        list.mapNotNull { dto ->
            val issueNo = dto.issueNo ?: dto.issue ?: dto.expect ?: return@mapNotNull null
            val date = dto.date ?: dto.openDate ?: dto.time ?: ""
            val front = (dto.frontNumbers ?: dto.red ?: dto.redBall).orEmpty()
            val back = (dto.backNumbers ?: dto.blue ?: dto.blueBall).orEmpty()
            LotteryDraw(
                type = type.name,
                issueNo = issueNo,
                date = date,
                frontNumbers = normalizeNumbers(front),
                backNumbers = normalizeNumbers(back),
                poolAmount = dto.poolAmount ?: dto.pool,
                firstCount = dto.firstCount,
                firstPrize = dto.firstPrize,
                secondCount = dto.secondCount,
                secondPrize = dto.secondPrize,
            )
        }.distinctBy { it.issueNo }
    }

    private fun normalizeNumbers(raw: String): String {
        // 兼容 "01 02 03"、"01,02,03"、"01+02+03"、"01, 02, 03"
        val parts = raw.split(',', ' ', '+', '|', '，').map { it.trim() }.filter { it.isNotEmpty() }
        return parts.joinToString(",") { it }
    }

    companion object {
        /**
         * 将内部 [LotteryType] 与服务端 lottery code 做映射。
         * 请根据实际接口文档调整。
         */
        fun LotteryType.toServerCode(): String = when (this) {
            LotteryType.SSQ -> "ssq"
            LotteryType.DLT -> "dlt"
        }
    }
}
