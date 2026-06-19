package com.lzk.common.servicce.device

import android.content.Context
import com.alibaba.android.arouter.facade.template.IProvider
import com.alibaba.android.arouter.launcher.ARouter
import com.lzk.common.bean.device.ConnectionState
import com.lzk.common.bean.device.LettinGatewayInfo
import kotlinx.coroutines.flow.SharedFlow

interface DeviceService : IProvider {
    val gatewayFlow: SharedFlow<List<LettinGatewayInfo>>
    val connectionStateFlow: SharedFlow<ConnectionState>

    override fun init(context: Context?) {}

    fun syncGateway()

    fun connect(
        ip: String,
        port: Int,
    )

    fun syncGwTable(
        gwMac: String,
        ip: String,
    )
}

fun getDeviceService(): DeviceService = ARouter.getInstance().navigation(DeviceService::class.java)
