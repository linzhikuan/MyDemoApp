package com.lzk.demo.lettin.device.impl

import com.alibaba.android.arouter.facade.annotation.Route
import com.lzk.common.bean.device.LettinGatewayInfo
import com.lzk.common.servicce.CommonServiceConstants
import com.lzk.common.servicce.device.DeviceService
import com.lzk.demo.lettin.device.DeviceManager
import kotlinx.coroutines.flow.SharedFlow

@Route(path = CommonServiceConstants.Service.DEVICE)
class LettinDeviceServiceImpl : DeviceService {
    override val gatewayFlow: SharedFlow<List<LettinGatewayInfo>>
        get() = DeviceManager.instance.gatewayFlow

    override fun syncGateway(): Boolean {
        DeviceManager.instance.syncGateway()
        return true
    }
}
