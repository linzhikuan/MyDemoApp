package com.lzk.demo.lettin.device.utils

import com.lzk.demo.lettin.device.Constants
import org.json.JSONArray
import org.json.JSONObject
import kotlin.random.Random

object GwParamUtils {
    fun syncGwTable(gwMac: String): String {
        val childTables =
            listOf<Int>(
                Constants.LETTIN4_GW_T,
                Constants.LETTIN4_DEV_T,
                Constants.LETTIN4_ROOM_T,
                Constants.LETTIN4_AREA_T,
                Constants.LETTIN4_SNAPSHOT_T,
                Constants.LETTIN4_SCENE_T,
                Constants.LETTIN4_APPRES_T,
            )
        return JSONObject()
            .apply {
                put("Tid", Random.nextInt(32767))
                put("Cmd", Constants.CMD_TABLE_QUERY)
                put("gwId", gwMac)
                val jsonArray = JSONArray()
                childTables.forEach {
                    val obj = JSONObject()
                    obj.put("tableId", it)
                    obj.put("ver", 0)
                    jsonArray.put(obj)
                }
                put("data", jsonArray)
                put("Token", "lettintesttokena")
            }.toString()
    }
}
