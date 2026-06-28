package com.lzk.lettin.business.main.data.repository

import com.lzk.lettin.business.main.data.local.LotteryDrawDao
import com.lzk.lettin.business.main.data.local.SavedTicketDao
import com.lzk.lettin.business.main.data.model.LotteryDraw
import com.lzk.lettin.business.main.data.model.LotteryType
import com.lzk.lettin.business.main.data.model.SavedTicket
import com.lzk.lettin.business.main.data.remote.LotteryRemoteDataSource
import com.lzk.core.log.logE
import com.lzk.core.log.logI
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * 数据仓库：协调 remote + local。
 * - UI 端始终订阅 [observeLatest] / [observeSavedTickets] 等 Flow
 * - 通过 [refresh] 从远程/ mock 拉取最新并写入本地
 */
class LotteryRepository @Inject constructor(
    private val localDrawDao: LotteryDrawDao,
    private val remoteDataSource: LotteryRemoteDataSource,
    private val savedTicketDao: SavedTicketDao,
) {

    fun observeLatest(type: LotteryType, limit: Int = 50): Flow<List<LotteryDraw>> =
        localDrawDao.observeLatest(type.name, limit)

    suspend fun getLatest(type: LotteryType, limit: Int = 50): List<LotteryDraw> =
        localDrawDao.getLatest(type.name, limit)

    /**
     * 从远程刷新数据，失败时保留本地现有数据（不抛异常）。
     * @return 本次刷新到的条数
     */
    suspend fun refresh(type: LotteryType, count: Int = 30): Int {
        return runCatching {
            val list = remoteDataSource.fetchLatest(type, count)
            if (list.isNotEmpty()) {
                localDrawDao.upsertAll(list)
            }
            list.size
        }.onFailure {
            logE("LotteryRepository", "refresh ${type.name} 失败: ${it.message}")
        }.getOrDefault(0)
    }

    fun observeSavedTickets(type: LotteryType? = null): Flow<List<SavedTicket>> =
        type?.let { savedTicketDao.observeByType(it.name) } ?: savedTicketDao.observeAll()

    suspend fun saveTicket(ticket: SavedTicket): Long = savedTicketDao.insert(ticket)

    suspend fun deleteTicket(ticket: SavedTicket) = savedTicketDao.delete(ticket)

    suspend fun deleteTicketById(id: Long) = savedTicketDao.deleteById(id)

    /** 在应用启动时调用，保证首次启动也能展示 mock 数据。*/
    suspend fun ensureMockDataForAll() {
        for (t in LotteryType.values()) {
            val exists = localDrawDao.getLatest(t.name, 1).isNotEmpty()
            if (!exists) {
                val count = refresh(t, 30)
                logI("LotteryRepository", "首次填充 mock 数据 ${t.name}: $count 条")
            }
        }
    }
}
