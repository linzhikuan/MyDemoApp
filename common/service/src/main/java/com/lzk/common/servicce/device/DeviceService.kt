package com.lzk.common.servicce.device

import android.content.Context
import com.alibaba.android.arouter.facade.template.IProvider
import com.alibaba.android.arouter.launcher.ARouter
import com.lzk.common.bean.device.LettinGatewayInfo
import kotlinx.coroutines.flow.SharedFlow

interface DeviceService : IProvider {
    val gatewayFlow: SharedFlow<List<LettinGatewayInfo>>

    override fun init(context: Context?) {}

    fun syncGateway(): Boolean
}

fun getDeviceService(): DeviceService = ARouter.getInstance().navigation(DeviceService::class.java)

    ?: throw IllegalStateException("DeviceService implementation not found. Make sure ARouter is initialized and the implementation module is included.")
