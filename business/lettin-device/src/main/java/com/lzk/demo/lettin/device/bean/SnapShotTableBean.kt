package com.lzk.demo.lettin.device.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "snapshot_table")
data class SnapShotTableBean(
    @PrimaryKey
    @ColumnInfo(name = "snapshot_id")
    val snapshotId: Int,
    @ColumnInfo(name = "hidden")
    val hidden: Int = 0,
    @ColumnInfo(name = "icon_id")
    val iconId: Int = 0,
    @ColumnInfo(name = "locked")
    val locked: Int = 0,
    @ColumnInfo(name = "uname")
    val uname: String = "",
    @ColumnInfo(name = "creat_time")
    val creatTime: String = "",
    @ColumnInfo(name = "creater")
    val creater: String = "",
    @ColumnInfo(name = "dev_port_id_set")
    val devPortIdSet: List<Int> = emptyList(),
)