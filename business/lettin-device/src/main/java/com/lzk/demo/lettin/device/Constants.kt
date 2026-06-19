package com.lzk.demo.lettin.device

object Constants {
    const val UDP_LOCAL_PORT = 6000
    const val UDP_REMOTE_PORT = 7000

    const val LETTIN4_GW_T = 1 // 网关信息表
    const val LETTIN4_DEV_T = 2 // 设备表
    const val LETTIN4_ROOM_T = 3 // 房间表
    const val LETTIN4_AREA_T = 4 // 区域表
    const val LETTIN4_SNAPSHOT_T = 5 // 快照表
    const val LETTIN4_SCENE_T = 6 // 场景表
    const val LETTIN4_APPRES_T = 7 // 预留字段表

    const val CMD_TABLE_QUERY = 4004 // 查询表操作

    const val BROADCAST_IP = "255.255.255.255"
}
