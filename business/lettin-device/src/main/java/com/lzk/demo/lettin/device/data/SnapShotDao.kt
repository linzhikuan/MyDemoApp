package com.lzk.demo.lettin.device.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lzk.demo.lettin.device.bean.SnapShotTableBean

@Dao
interface SnapShotDao {
    @Query("SELECT * FROM snapshot_table")
    fun getAll(): List<SnapShotTableBean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(snapshots: List<SnapShotTableBean>)

    @Query("DELETE FROM snapshot_table")
    fun deleteAll()
}
