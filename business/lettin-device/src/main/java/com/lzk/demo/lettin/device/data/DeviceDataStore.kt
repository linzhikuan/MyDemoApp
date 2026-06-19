package com.lzk.demo.lettin.device.data

import android.content.Context
import androidx.room.Room
import com.lzk.demo.lettin.device.PublicKeys
import com.lzk.demo.lettin.device.bean.DeviceTableBean

object DeviceDataStore {
    private var database: DeviceDatabase? = null
    private var deviceDao: DeviceDao? = null

    fun init(context: Context) {
        database =
            Room
                .databaseBuilder(
                    context.applicationContext,
                    DeviceDatabase::class.java,
                    PublicKeys.KEY_ROOM_DEVICE_TABLE,
                ).build()
        deviceDao = database!!.deviceDao()
    }

    fun updateTable(deviceTables: List<DeviceTableBean>?) {
        deviceTables?.let {
            deviceDao?.deleteAll()
            deviceDao?.insertAll(it)
        }
    }

    fun getDeviceTables(): List<DeviceTableBean> = deviceDao?.getAll() ?: emptyList()
}
