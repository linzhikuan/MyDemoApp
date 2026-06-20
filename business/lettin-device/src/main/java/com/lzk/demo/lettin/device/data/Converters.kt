package com.lzk.demo.lettin.device.data

import androidx.room.TypeConverter
import com.lzk.core.utils.GsonUtils
import com.lzk.demo.lettin.device.bean.ActionItem
import com.lzk.demo.lettin.device.bean.AppInfo
import com.lzk.demo.lettin.device.bean.BasicInfo
import com.lzk.demo.lettin.device.bean.DeviceBasicInfo
import com.lzk.demo.lettin.device.bean.DevicePowerSource
import com.lzk.demo.lettin.device.bean.NodeItem
import com.lzk.demo.lettin.device.bean.NwkAddrInfo
import com.lzk.demo.lettin.device.bean.OnOff
import com.lzk.demo.lettin.device.bean.PortFeature
import com.lzk.demo.lettin.device.bean.PowerSource
import com.lzk.demo.lettin.device.bean.SceneDes

class Converters {
    @TypeConverter
    fun fromPortFeature(value: PortFeature?): String? = value?.let { GsonUtils.toJson(it) }

    @TypeConverter
    fun toPortFeature(value: String?): PortFeature? =
        value?.let {
            runCatching {
                GsonUtils.fromJson(
                    it,
                    PortFeature::class.java,
                )
            }.getOrNull()
        }

    @TypeConverter
    fun fromOnOff(value: OnOff?): String? = value?.let { GsonUtils.toJson(it) }

    @TypeConverter
    fun toOnOff(value: String?): OnOff? = value?.let { runCatching { GsonUtils.fromJson(it, OnOff::class.java) }.getOrNull() }

    @TypeConverter
    fun fromIntList(value: List<Int>?): String? = value?.let { GsonUtils.toJson(it) }

    @TypeConverter
    fun toIntList(value: String?): List<Int>? =
        value?.let {
            runCatching {
                GsonUtils.fromJsonToList(
                    it,
                    Int::class.java,
                )
            }.getOrNull()
        }

    @TypeConverter
    fun fromSceneDes(value: SceneDes?): String? = value?.let { GsonUtils.toJson(it) }

    @TypeConverter
    fun toSceneDes(value: String?): SceneDes? = value?.let { runCatching { GsonUtils.fromJson(it, SceneDes::class.java) }.getOrNull() }

    @TypeConverter
    fun fromNodeItemList(value: List<NodeItem>?): String? = value?.let { GsonUtils.toJson(it) }

    @TypeConverter
    fun toNodeItemList(value: String?): List<NodeItem>? =
        value?.let {
            runCatching {
                GsonUtils.fromJsonToList(
                    it,
                    NodeItem::class.java,
                )
            }.getOrNull()
        }

    @TypeConverter
    fun fromActionItemList(value: List<ActionItem>?): String? = value?.let { GsonUtils.toJson(it) }

    @TypeConverter
    fun toActionItemList(value: String?): List<ActionItem>? =
        value?.let {
            runCatching {
                GsonUtils.fromJsonToList(
                    it,
                    ActionItem::class.java,
                )
            }.getOrNull()
        }

    @TypeConverter
    fun fromAppInfo(value: AppInfo?): String? = value?.let { GsonUtils.toJson(it) }

    @TypeConverter
    fun toAppInfo(value: String?): AppInfo? = value?.let { runCatching { GsonUtils.fromJson(it, AppInfo::class.java) }.getOrNull() }

    @TypeConverter
    fun fromAnyList(value: List<Any>?): String? = value?.let { GsonUtils.toJson(it) }

    @TypeConverter
    fun toAnyList(value: String?): List<Any>? =
        value?.let {
            runCatching {
                GsonUtils.fromJsonToList(
                    it,
                    Any::class.java,
                )
            }.getOrNull()
        }

    @TypeConverter
    fun fromBasicInfo(value: BasicInfo?): String? = value?.let { GsonUtils.toJson(it) }

    @TypeConverter
    fun toBasicInfo(value: String?): BasicInfo? =
        value?.let {
            runCatching {
                GsonUtils.fromJson(it, BasicInfo::class.java)
            }.getOrNull()
        }

    @TypeConverter
    fun fromPowerSource(value: PowerSource?): String? = value?.let { GsonUtils.toJson(it) }

    @TypeConverter
    fun toPowerSource(value: String?): PowerSource? =
        value?.let {
            runCatching {
                GsonUtils.fromJson(it, PowerSource::class.java)
            }.getOrNull()
        }

    @TypeConverter
    fun fromDeviceBasicInfo(value: DeviceBasicInfo?): String? = value?.let { GsonUtils.toJson(it) }

    @TypeConverter
    fun toDeviceBasicInfo(value: String?): DeviceBasicInfo? =
        value?.let {
            runCatching {
                GsonUtils.fromJson(it, DeviceBasicInfo::class.java)
            }.getOrNull()
        }

    @TypeConverter
    fun fromDevicePowerSource(value: DevicePowerSource?): String? = value?.let { GsonUtils.toJson(it) }

    @TypeConverter
    fun toDevicePowerSource(value: String?): DevicePowerSource? =
        value?.let {
            runCatching {
                GsonUtils.fromJson(it, DevicePowerSource::class.java)
            }.getOrNull()
        }

    @TypeConverter
    fun fromNwkAddrInfo(value: NwkAddrInfo?): String? = value?.let { GsonUtils.toJson(it) }

    @TypeConverter
    fun toNwkAddrInfo(value: String?): NwkAddrInfo? =
        value?.let {
            runCatching {
                GsonUtils.fromJson(it, NwkAddrInfo::class.java)
            }.getOrNull()
        }
}
