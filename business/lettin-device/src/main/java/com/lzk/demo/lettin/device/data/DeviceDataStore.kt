package com.lzk.demo.lettin.device.data

import android.content.Context
import com.lzk.demo.lettin.device.bean.AreaTableBean
import com.lzk.demo.lettin.device.bean.DeviceTableBean
import com.lzk.demo.lettin.device.bean.GwTableBean
import com.lzk.demo.lettin.device.bean.RoomTableBean
import com.lzk.demo.lettin.device.bean.SceneTableBean
import com.lzk.demo.lettin.device.bean.SnapShotTableBean

object DeviceDataStore {
    private var database: DeviceDatabase? = null

    private var deviceDao: DeviceDao? = null
    private var areaDao: AreaDao? = null
    private var gwDao: GwDao? = null
    private var roomDao: RoomDao? = null
    private var sceneDao: SceneDao? = null
    private var snapShotDao: SnapShotDao? = null

    fun init(context: Context) {
        database = androidx.room.Room.databaseBuilder(
            context.applicationContext,
            DeviceDatabase::class.java,
            "device_db"
        ).build()
        deviceDao = database!!.deviceDao()
        areaDao = database!!.areaDao()
        gwDao = database!!.gwDao()
        roomDao = database!!.roomDao()
        sceneDao = database!!.sceneDao()
        snapShotDao = database!!.snapShotDao()
    }

    // Device
    fun updateDeviceTable(devices: List<DeviceTableBean>?) {
        devices?.let {
            deviceDao?.deleteAll()
            deviceDao?.insertAll(it)
        }
    }

    fun getDeviceTables(): List<DeviceTableBean> {
        return deviceDao?.getAll() ?: emptyList()
    }

    // Area
    fun updateAreaTable(areas: List<AreaTableBean>?) {
        areas?.let {
            areaDao?.deleteAll()
            areaDao?.insertAll(it)
        }
    }

    fun getAreaTables(): List<AreaTableBean> {
        return areaDao?.getAll() ?: emptyList()
    }

    // Gateway
    fun updateGwTable(gateways: List<GwTableBean>?) {
        gateways?.let {
            gwDao?.deleteAll()
            gwDao?.insertAll(it)
        }
    }

    fun getGwTables(): List<GwTableBean> {
        return gwDao?.getAll() ?: emptyList()
    }

    // Room
    fun updateRoomTable(rooms: List<RoomTableBean>?) {
        rooms?.let {
            roomDao?.deleteAll()
            roomDao?.insertAll(it)
        }
    }

    fun getRoomTables(): List<RoomTableBean> {
        return roomDao?.getAll() ?: emptyList()
    }

    // Scene
    fun updateSceneTable(scenes: List<SceneTableBean>?) {
        scenes?.let {
            sceneDao?.deleteAll()
            sceneDao?.insertAll(it)
        }
    }

    fun getSceneTables(): List<SceneTableBean> {
        return sceneDao?.getAll() ?: emptyList()
    }

    // Snapshot
    fun updateSnapShotTable(snapshots: List<SnapShotTableBean>?) {
        snapshots?.let {
            snapShotDao?.deleteAll()
            snapShotDao?.insertAll(it)
        }
    }

    fun getSnapShotTables(): List<SnapShotTableBean> {
        return snapShotDao?.getAll() ?: emptyList()
    }
}