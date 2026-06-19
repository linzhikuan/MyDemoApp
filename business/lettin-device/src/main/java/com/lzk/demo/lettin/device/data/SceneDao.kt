package com.lzk.demo.lettin.device.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lzk.demo.lettin.device.bean.SceneTableBean

@Dao
interface SceneDao {
    @Query("SELECT * FROM scene_table")
    fun getAll(): List<SceneTableBean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(scenes: List<SceneTableBean>)

    @Query("DELETE FROM scene_table")
    fun deleteAll()
}