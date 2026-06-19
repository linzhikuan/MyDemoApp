package com.lzk.demo.lettin.device.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lzk.demo.lettin.device.bean.GwTableBean

@Dao
interface GwDao {
    @Query("SELECT * FROM gw_table")
    fun getAll(): List<GwTableBean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(gateways: List<GwTableBean>)

    @Query("DELETE FROM gw_table")
    fun deleteAll()
}
