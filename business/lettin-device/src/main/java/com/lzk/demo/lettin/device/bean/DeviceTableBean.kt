package com.lzk.demo.lettin.device.bean

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "device_table")
data class DeviceTableBean(
    @PrimaryKey @ColumnInfo(name = "device_id") val deviceId: String,
    @ColumnInfo(name = "room_id") val roomId: Int,
    @ColumnInfo(name = "area_id") val areaId: Int,
    @Embedded val portFeature: PortFeature?,
)

data class PortFeature(
    @Embedded val onoff: OnOff?,
)

data class OnOff(
    val value: Int,
)
