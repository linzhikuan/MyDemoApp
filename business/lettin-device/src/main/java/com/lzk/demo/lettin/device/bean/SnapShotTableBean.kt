package com.lzk.demo.lettin.device.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "snapshot_table")
data class SnapShotTableBean(
    @PrimaryKey
    @ColumnInfo(name = "snapshot_id")
    val snapshotId: Int,
)
