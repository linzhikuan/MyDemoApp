package com.lzk.demo.lettin.device.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gw_table")
data class GwTableBean(
    @PrimaryKey
    @ColumnInfo(name = "gateway_id")
    val gatewayId: String,
)
