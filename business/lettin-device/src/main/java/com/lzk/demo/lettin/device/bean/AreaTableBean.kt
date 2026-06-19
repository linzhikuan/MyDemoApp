package com.lzk.demo.lettin.device.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "area_table")
data class AreaTableBean(
    @PrimaryKey
    @ColumnInfo(name = "area_id")
    val areaId: Int,
    @ColumnInfo(name = "room_id")
    val roomId: Int = 0,
    @ColumnInfo(name = "icon_id")
    val iconId: Int = 0,
    @ColumnInfo(name = "locked")
    val locked: Int = 0,
    @ColumnInfo(name = "uname")
    val uname: String = "",
)