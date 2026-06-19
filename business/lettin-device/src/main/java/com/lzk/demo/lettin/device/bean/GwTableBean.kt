package com.lzk.demo.lettin.device.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gw_table")
data class GwTableBean(
    @PrimaryKey
    @ColumnInfo(name = "gateway_id")
    val gatewayId: String,
    @ColumnInfo(name = "basic_info")
    val basicInfo: BasicInfo? = null,
    @ColumnInfo(name = "territory_id")
    val territoryId: Int = 0,
    @ColumnInfo(name = "gateway_button")
    val gatewayButton: Int = 0,
    @ColumnInfo(name = "time_zone")
    val timeZone: Int = 0,
    @ColumnInfo(name = "country")
    val country: String = "",
    @ColumnInfo(name = "city")
    val city: String = "",
    @ColumnInfo(name = "name")
    val name: String = "",
    @ColumnInfo(name = "indicator_led")
    val indicatorLED: Int = 0,
    @ColumnInfo(name = "power_source")
    val powerSource: PowerSource? = null,
    @ColumnInfo(name = "wifi_auto_hide")
    val wifiAutoHide: Int = 0,
    @ColumnInfo(name = "lettin_edge")
    val lettinEdge: Int = 0,
    @ColumnInfo(name = "connected")
    val connected: Int = 0,
    @ColumnInfo(name = "ct_lock")
    val CTLock: Int = 0,
    @ColumnInfo(name = "log_flag")
    val logFlag: Int = 0,
    @ColumnInfo(name = "log_level")
    val logLevel: Int = 0,
)

data class BasicInfo(
    val seqNum: String = "",
    val wifiMac: String = "",
    val ssid: String = "",
    val productName: String = "",
    val manufacture: String = "",
    val model: String = "",
    val version: String = "",
    val nwkVersion: String = "",
    val swVer: String = "",
    val hwVer: String = "",
)

data class PowerSource(
    val psCategory: Int = 0,
)
