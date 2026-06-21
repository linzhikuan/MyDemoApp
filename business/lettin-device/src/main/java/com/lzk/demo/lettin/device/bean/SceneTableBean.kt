package com.lzk.demo.lettin.device.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scene_table")
data class SceneTableBean(
    @PrimaryKey
    @ColumnInfo(name = "scene_id")
    val sceneId: Int,
    @ColumnInfo(name = "type")
    val type: Int = 0,
    @ColumnInfo(name = "onoff")
    val onoff: Int = 0,
    @ColumnInfo(name = "icon_id")
    val iconId: Int = 0,
    @ColumnInfo(name = "mode")
    val mode: Int = 0,
    @ColumnInfo(name = "uname")
    val uname: String = "",
    @ColumnInfo(name = "child_ids")
    val childIds: List<Int> = emptyList(),
    @ColumnInfo(name = "value_ref")
    val valueRef: List<Any> = emptyList(),
    @ColumnInfo(name = "control_id")
    val controlId: Int = 0,
    @ColumnInfo(name = "des")
    val des: SceneDes? = null,
    @ColumnInfo(name = "node_array")
    val nodeArray: List<NodeItem> = emptyList(),
)

data class SceneDes(
    val app: AppInfo? = null,
    val crateTime: String = "",
    val modifiableByApp: Boolean = false,
    val user: String = "",
)

data class AppInfo(
    val bgIndex: Int = 0,
    val des: String = "",
)

data class NodeItem(
    val nodeId: Int = 0,
    val enable: Int = 0,
    val delay: Int = 0,
    val cdtype: Int = 0,
    val childIds: List<Int> = emptyList(),
    val condition: List<Any> = emptyList(),
    val action: List<ActionItem> = emptyList(),
)

data class ActionItem(
    val actype: Int = 0,
    val runTime: Int = 0,
)