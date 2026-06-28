package com.lzk.lettin.business.main.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.lzk.lettin.business.main.data.model.SavedTicket
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedTicketDao {

    @Query("SELECT * FROM saved_ticket WHERE type = :type ORDER BY createdAt DESC")
    fun observeByType(type: String): Flow<List<SavedTicket>>

    @Query("SELECT * FROM saved_ticket ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<SavedTicket>>

    @Insert
    suspend fun insert(ticket: SavedTicket): Long

    @Delete
    suspend fun delete(ticket: SavedTicket)

    @Query("DELETE FROM saved_ticket WHERE id = :id")
    suspend fun deleteById(id: Long)
}
