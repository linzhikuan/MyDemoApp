package com.lzk.demo.lettin.device.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lzk.demo.lettin.device.bean.RoomTableBean

@Dao
interface RoomDao {
    @Query("SELECT * FROM room_table")
    fun getAll(): List<RoomTableBean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(rooms: List<RoomTableBean>)

    @Query("DELETE FROM room_table")
    fun deleteAll()
}
