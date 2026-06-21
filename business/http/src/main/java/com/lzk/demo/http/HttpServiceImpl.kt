package com.lzk.demo.http

import com.alibaba.android.arouter.facade.annotation.Route
import com.lzk.common.servicce.CommonServiceConstants
import com.lzk.common.servicce.http.HttpService

@Route(path = CommonServiceConstants.Service.HTTP)
class HttpServiceImpl : HttpService {
    override fun <T> getService(
        service: Class<T>,
        baseUrl: String,
    ): T = HttpManager.createHttpService(baseUrl).create<T>(service)
}
