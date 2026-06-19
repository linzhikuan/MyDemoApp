package com.lzk.demo.lettin.device.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "room_table")
data class RoomTableBean(
    @PrimaryKey
    @ColumnInfo(name = "room_id")
    val roomId: Int,
    @ColumnInfo(name = "icon_id")
    val iconId: Int = 0,
    @ColumnInfo(name = "locked")
    val locked: Int = 0,
    @ColumnInfo(name = "create_times")
    val createTimes: Int = 0,
    @ColumnInfo(name = "state")
    val state: String = "",
    @ColumnInfo(name = "uname")
    val uname: String = "",
)
