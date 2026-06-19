package com.lzk.demo.lettin.device.data

import com.lzk.core.mmkv.MMKVManager
import com.lzk.core.utils.GsonUtils
import com.lzk.demo.lettin.device.PublicKeys
import com.lzk.demo.lettin.device.bean.DeviceTableBean

object DeviceDataStore {
    private val mmkv = MMKVManager.mmkvWithID(PublicKeys.KEY_MMKV_DEVICE_TABLE)

    fun updateTable(deviceTables: List<DeviceTableBean>?) {
        mmkv.putString(DeviceTableBean::class.simpleName, GsonUtils.toJson(deviceTables))
    }

    fun getDeviceTables(): List<DeviceTableBean>? =
        runCatching {
            val value = mmkv.getString(DeviceTableBean::class.simpleName, "")
            GsonUtils.fromJsonToList(value, DeviceTableBean::class.java)
        }.getOrNull()
}
