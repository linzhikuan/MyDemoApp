package com.lzk.lettin.business.main.data.remote

import com.lzk.lettin.business.main.data.model.LotteryDraw
import com.lzk.lettin.business.main.data.model.LotteryType
import com.lzk.lettin.business.main.data.remote.api.LotteryApi
import com.lzk.lettin.business.main.data.remote.api.LotteryDrawDto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

/**
 * 真实 API 数据源（huiniao.top）。
 *
 * 服务器返回的一期开奖记录是按数字字段展开的（one/two/three/...），
 * 这里根据 [LotteryType] 选取对应位数做 front/back 切分：
 *   - SSQ：front=one..six（共 6 位，即红球），back=seven（共 1 位，即蓝球）
 *   - DLT：front=one..five（共 5 位），back=six..seven（共 2 位）
 *
 * 最终输出到本地数据库时，号码统一格式为 "01,02,03"，与已有 mock 数据一致。
 */
class RealLotteryRemoteDataSource(
    private val api: LotteryApi,
    private val ioDispatcher: CoroutineDispatcher,
) : LotteryRemoteDataSource {
    override suspend fun fetchLatest(
        type: LotteryType,
        count: Int,
    ): List<LotteryDraw> =
        withContext(ioDispatcher) {
            val response = api.getHistory(type = type.toServerCode(), limit = count)
            if (response?.code != 1) return@withContext emptyList()

            val list =
                response.data
                    ?.data
                    ?.list
                    .orEmpty()

            list
                .mapNotNull { dto ->
                    val issueNo = dto.issueNo?.trim().orEmpty()
                    if (issueNo.isEmpty()) return@mapNotNull null

                    val date = dto.date.orEmpty()

                    val front = dto.pickFrontNumbers(type)
                    val back = dto.pickBackNumbers(type)

                    LotteryDraw(
                        type = type.name,
                        issueNo = issueNo,
                        date = date,
                        frontNumbers = front,
                        backNumbers = back,
                        poolAmount = dto.poolMoney,
                        firstCount = null,
                        firstPrize = null,
                        secondCount = null,
                        secondPrize = null,
                    )
                }.distinctBy { it.issueNo }
        }

    // ---------- 号码拆分/拼接 ----------

    private fun LotteryDrawDto.pickFrontNumbers(type: LotteryType): String {
        val slots =
            when (type) {
                LotteryType.SSQ -> listOfNotNull(one, two, three, four, five, six)
                LotteryType.DLT -> listOfNotNull(one, two, three, four, five)
            }
        return slots.filterNotNull().joinToString(",") { it.toTwoDigit() }
    }

    private fun LotteryDrawDto.pickBackNumbers(type: LotteryType): String {
        val slots =
            when (type) {
                LotteryType.SSQ -> listOfNotNull(seven)
                LotteryType.DLT -> listOfNotNull(six, seven)
            }
        return slots.filterNotNull().joinToString(",") { it.toTwoDigit() }
    }

    private fun Int.toTwoDigit(): String = if (this in 0..9) "0$this" else "$this"

    companion object {
        /**
         * 将内部 [LotteryType] 与 huiniao.top 的彩种 code 做映射。
         */
        fun LotteryType.toServerCode(): String =
            when (this) {
                LotteryType.SSQ -> "ssq"
                LotteryType.DLT -> "dlt"
            }
    }
}
