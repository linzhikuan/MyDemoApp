package com.lzk.demo.lettin.device.impl

import com.lzk.common.servicce.http.getHttpService
import com.lzk.core.log.logD
import com.lzk.core.log.logE
import com.lzk.core.network.toJsonRequestBody
import com.lzk.core.utils.GsonUtils
import com.lzk.demo.lettin.device.Constants
import com.lzk.demo.lettin.device.bean.AreaTableBean
import com.lzk.demo.lettin.device.bean.DeviceTableBean
import com.lzk.demo.lettin.device.bean.RoomTableBean
import com.lzk.demo.lettin.device.bean.SceneTableBean
import com.lzk.demo.lettin.device.bean.SnapShotTableBean
import com.lzk.demo.lettin.device.inner.LettinAPI
import com.lzk.demo.lettin.device.inner.LettinGwHelper
import com.lzk.demo.lettin.device.utils.GwParamUtils

class LettinGwHelperImpl : LettinGwHelper {
    companion object {
        private const val TAG = "LettinGwHelperImpl"
    }

    override suspend fun syncTable(
        ip: String,
        gwMac: String,
    ) {
        runCatching {
            getHttpService().getService(LettinAPI::class.java, ip).syncGwTable(
                GwParamUtils.syncGwTable(gwMac).toJsonRequestBody(),
            )
        }.onSuccess {
            it.data?.forEach { tableBean ->
                logD(TAG, "tableId:${tableBean.tableId},${tableBean.tableArray}")
                val tableArrayJson = GsonUtils.toJson(tableBean.tableArray)
                when (tableBean.tableId) {
                    Constants.LETTIN4_DEV_T -> {
                        GsonUtils.fromJsonToList<DeviceTableBean>(
                            tableArrayJson,
                            DeviceTableBean::class.java,
                        )
                    }

                    Constants.LETTIN4_ROOM_T -> {
                        GsonUtils.fromJson<RoomTableBean>(
                            tableArrayJson,
                            RoomTableBean::class.java,
                        )
                    }

                    Constants.LETTIN4_AREA_T -> {
                        GsonUtils.fromJson<AreaTableBean>(
                            tableArrayJson,
                            AreaTableBean::class.java,
                        )
                    }

                    Constants.LETTIN4_SNAPSHOT_T -> {
                        GsonUtils.fromJson<SnapShotTableBean>(
                            tableArrayJson,
                            SnapShotTableBean::class.java,
                        )
                    }

                    Constants.LETTIN4_SCENE_T -> {
                        GsonUtils.fromJson<SceneTableBean>(
                            tableArrayJson,
                            SceneTableBean::class.java,
                        )
                    }
                }
            }
        }.onFailure {
            logE(TAG, "syncTableError", it)
        }
    }
}
