package com.lzk.demo.lettin.device.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lzk.demo.lettin.device.bean.AreaTableBean

@Dao
interface AreaDao {
    @Query("SELECT * FROM area_table")
    fun getAll(): List<AreaTableBean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(areas: List<AreaTableBean>)

    @Query("DELETE FROM area_table")
    fun deleteAll()
}
