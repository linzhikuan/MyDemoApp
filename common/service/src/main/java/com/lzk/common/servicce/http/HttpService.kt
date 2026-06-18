package com.lzk.common.servicce.http

import android.content.Context
import com.alibaba.android.arouter.facade.template.IProvider
import com.alibaba.android.arouter.launcher.ARouter

interface HttpService : IProvider {
    override fun init(context: Context?) {}

    fun <T> getService(
        service: Class<T>,
        baseUrl: String,
    ): T
}

fun getHttpService(): HttpService = ARouter.getInstance().navigation(HttpService::class.java)
