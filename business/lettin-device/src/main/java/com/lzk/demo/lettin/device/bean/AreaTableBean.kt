package com.lzk.demo.lettin.device.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "area_table")
data class AreaTableBean(
    @PrimaryKey
    @ColumnInfo(name = "area_id")
    val areaId: Int,
)
