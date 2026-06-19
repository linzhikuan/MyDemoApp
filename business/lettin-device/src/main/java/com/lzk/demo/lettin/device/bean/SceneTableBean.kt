package com.lzk.demo.lettin.device.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scene_table")
data class SceneTableBean(
    @PrimaryKey
    @ColumnInfo(name = "scene_id")
    val sceneId: Int,
)
