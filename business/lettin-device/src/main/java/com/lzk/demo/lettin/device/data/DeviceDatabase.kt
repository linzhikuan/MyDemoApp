package com.lzk.demo.lettin.device.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lzk.demo.lettin.device.bean.AreaTableBean
import com.lzk.demo.lettin.device.bean.DeviceTableBean
import com.lzk.demo.lettin.device.bean.GwTableBean
import com.lzk.demo.lettin.device.bean.RoomTableBean
import com.lzk.demo.lettin.device.bean.SceneTableBean
import com.lzk.demo.lettin.device.bean.SnapShotTableBean

@Database(
    entities = [
        DeviceTableBean::class,
        AreaTableBean::class,
        GwTableBean::class,
        RoomTableBean::class,
        SceneTableBean::class,
        SnapShotTableBean::class,
    ],
    version = 1,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class DeviceDatabase : RoomDatabase() {
    abstract fun deviceDao(): DeviceDao

    abstract fun areaDao(): AreaDao

    abstract fun gwDao(): GwDao

    abstract fun roomDao(): RoomDao

    abstract fun sceneDao(): SceneDao

    abstract fun snapShotDao(): SnapShotDao
}
