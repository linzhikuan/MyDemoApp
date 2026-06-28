package com.lzk.lettin.business.main.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lzk.lettin.business.main.data.model.LotteryDraw
import kotlinx.coroutines.flow.Flow

@Dao
interface LotteryDrawDao {

    @Query("SELECT * FROM lottery_draw WHERE type = :type ORDER BY issueNo DESC LIMIT :limit")
    fun observeLatest(type: String, limit: Int = 50): Flow<List<LotteryDraw>>

    @Query("SELECT * FROM lottery_draw WHERE type = :type ORDER BY issueNo DESC LIMIT :limit")
    suspend fun getLatest(type: String, limit: Int = 50): List<LotteryDraw>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(list: List<LotteryDraw>)

    @Query("DELETE FROM lottery_draw WHERE type = :type")
    suspend fun deleteByType(type: String)
}
