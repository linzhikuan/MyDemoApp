package com.lzk.demo.lettin.device.impl

import com.lzk.common.servicce.http.getHttpService
import com.lzk.demo.lettin.device.inner.LettinAPI
import com.lzk.demo.lettin.device.inner.LettinGwHelper
import com.lzk.demo.lettin.device.utils.GwParamUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody

class LettinGwHelperImpl : LettinGwHelper {
    override suspend fun syncTable(
        ip: String,
        gwMac: String,
    ) {
        val param = GwParamUtils.syncGwTable(gwMac)
        getHttpService().getService(LettinAPI::class.java, ip).request(
            RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(),
                param,
            ),
        )
    }
}
