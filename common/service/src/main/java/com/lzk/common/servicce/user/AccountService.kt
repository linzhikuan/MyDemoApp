package com.lzk.common.servicce.user

import android.content.Context
import com.alibaba.android.arouter.facade.template.IProvider
import com.alibaba.android.arouter.launcher.ARouter

interface AccountService : IProvider {
    override fun init(context: Context?) {}

    suspend fun login(
        username: String,
        password: String,
    ): Boolean
}

fun getAccountService(): AccountService = ARouter.getInstance().navigation(AccountService::class.java)
