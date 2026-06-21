package com.lzk.demo.lettin.device.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "device_table")
data class DeviceTableBean(
    @PrimaryKey
    @ColumnInfo(name = "device_id")
    val deviceId: String,
    @ColumnInfo(name = "room_id")
    val roomId: Int,
    @ColumnInfo(name = "area_id")
    val areaId: Int,
    @ColumnInfo(name = "port_type")
    val portType: Int,
    @ColumnInfo(name = "port_id")
    val portId: Int,
    @ColumnInfo(name = "uname")
    val uname: String,
    @ColumnInfo(name = "icon_id")
    val iconId: Int,
    @ColumnInfo(name = "locked")
    val locked: Int,
    @ColumnInfo(name = "online")
    val online: Int,
    @ColumnInfo(name = "update_status")
    val updateStatus: Int,
    @ColumnInfo(name = "update_level")
    val updateLevel: Int,
    @ColumnInfo(name = "dev_index")
    val devIndex: Int,
    @ColumnInfo(name = "create_date")
    val createDate: Long,
    @ColumnInfo(name = "modif_count")
    val modifCount: Int,
    @ColumnInfo(name = "tag")
    val tag: String,
    @ColumnInfo(name = "groups")
    val groups: List<Int> = emptyList(),
    @ColumnInfo(name = "nwk_addr_info")
    val nwkAddrInfo: NwkAddrInfo?,
    @ColumnInfo(name = "basic_info")
    val basicInfo: DeviceBasicInfo?,
    @ColumnInfo(name = "power_source")
    val powerSource: DevicePowerSource?,
    @ColumnInfo(name = "port_feature")
    val portFeature: PortFeature?,
)

data class NwkAddrInfo(
    val nwkCategory: Int,
    val lettinNwkAddr: Int,
)

data class DeviceBasicInfo(
    val swVer: String,
    val version: String,
    val hwVer: String,
)

data class DevicePowerSource(
    val psCategory: Int,
    val batteryVtg: Float,
)

data class PortFeature(
    val onoff: OnOff?,
)

data class OnOff(
    val value: Int,
)
