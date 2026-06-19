package com.lzk.demo.lettin.device.impl

import com.lzk.common.servicce.http.getHttpService
import com.lzk.core.network.toJsonRequestBody
import com.lzk.demo.lettin.device.inner.LettinAPI
import com.lzk.demo.lettin.device.inner.LettinGwHelper
import com.lzk.demo.lettin.device.utils.GwParamUtils

class LettinGwHelperImpl : LettinGwHelper {
    override suspend fun syncTable(
        ip: String,
        gwMac: String,
    ) {
        getHttpService().getService(LettinAPI::class.java, ip).request(
            GwParamUtils.syncGwTable(gwMac).toJsonRequestBody(),
        )
    }
}
