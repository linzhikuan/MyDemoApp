package com.lzk.demo.lettin.device.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "room_table")
data class RoomTableBean(
    @PrimaryKey
    @ColumnInfo(name = "room_id")
    val roomId: Int,
)
