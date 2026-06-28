package com.lzk.lettin.business.main.data.remote

import com.lzk.lettin.business.main.data.model.LotteryType

/**
 * 远程数据源抽象。
 * 真实实现与 mock 实现都遵循该接口，
 * 只需在 [LotteryDataModule] 切换注入的实现类即可。
 */
interface LotteryRemoteDataSource {

    suspend fun fetchLatest(type: LotteryType, count: Int = 30): List<com.lzk.lettin.business.main.data.model.LotteryDraw>
}
