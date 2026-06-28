package com.lzk.lettin.business.main.data.remote

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lzk.lettin.business.main.data.model.LotteryDraw
import com.lzk.lettin.business.main.data.model.LotteryType
import com.lzk.core.log.logW
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 基于 assets JSON 的 mock 数据源，
 * 供无网络/开发阶段使用。真实 API 上线后只需替换实现类。
 */
class MockLotteryRemoteDataSource(
    private val context: Context,
    private val gson: Gson = Gson(),
) : LotteryRemoteDataSource {

    private data class RawDraw(
        val issueNo: String,
        val date: String,
        val frontNumbers: String,
        val backNumbers: String,
    )

    override suspend fun fetchLatest(type: LotteryType, count: Int): List<LotteryDraw> =
        withContext(Dispatchers.IO) {
            val assetFile = when (type) {
                LotteryType.SSQ -> "ssq_mock.json"
                LotteryType.DLT -> "dlt_mock.json"
            }
            runCatching {
                context.assets.open(assetFile).use { stream ->
                    val text = stream.bufferedReader().readText()
                    val listType = object : TypeToken<List<RawDraw>>() {}.type
                    val list: List<RawDraw> = gson.fromJson(text, listType)
                    list.take(count).map { raw ->
                        LotteryDraw(
                            type = type.name,
                            issueNo = raw.issueNo,
                            date = raw.date,
                            frontNumbers = raw.frontNumbers,
                            backNumbers = raw.backNumbers,
                        )
                    }
                }
            }.onFailure { logW("MockLotteryRemoteDataSource", "读取 asset $assetFile 失败: ${it.message}") }
                .getOrDefault(emptyList())
        }
}
