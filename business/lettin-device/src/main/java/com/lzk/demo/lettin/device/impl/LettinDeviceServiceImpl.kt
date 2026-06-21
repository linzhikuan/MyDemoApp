package com.lzk.demo.lettin.device.impl

import com.alibaba.android.arouter.facade.annotation.Route
import com.lzk.common.bean.device.ConnectionState
import com.lzk.common.bean.device.LettinGatewayInfo
import com.lzk.common.servicce.CommonServiceConstants
import com.lzk.common.servicce.device.DeviceService
import com.lzk.demo.lettin.device.DeviceManager
import kotlinx.coroutines.flow.SharedFlow

@Route(path = CommonServiceConstants.Service.DEVICE)
class LettinDeviceServiceImpl : DeviceService {
    override val gatewayFlow: SharedFlow<List<LettinGatewayInfo>>
        get() = DeviceManager.instance.gatewayFlow

    override val connectionStateFlow: SharedFlow<ConnectionState>
        get() = DeviceManager.instance.connectionStateFlow

    override fun syncGateway() {
        DeviceManager.instance.syncGateway()
    }

    override fun connect(
        ip: String,
        port: Int,
    ) {
        DeviceManager.instance.connectDevice(ip, port)
    }

    override suspend fun syncGwTable(
        gwMac: String,
        ip: String,
    ): Result<Unit> = DeviceManager.instance.syncGwTable(gwMac, ip)
}
