package com.lzk.demo.lettin.device.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lzk.demo.lettin.device.bean.DeviceTableBean

@Dao
interface DeviceDao {
    @Query("SELECT * FROM device_table")
    fun getAll(): List<DeviceTableBean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(devices: List<DeviceTableBean>)

    @Query("DELETE FROM device_table")
    fun deleteAll()
}
