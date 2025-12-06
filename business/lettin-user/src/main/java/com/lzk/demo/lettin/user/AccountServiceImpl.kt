package com.lzk.demo.lettin.user

import com.alibaba.android.arouter.facade.annotation.Route
import com.lzk.common.servicce.CommonServiceConstants
import com.lzk.common.servicce.user.AccountService

@Route(path = CommonServiceConstants.Service.ACCOUNT)
class AccountServiceImpl : AccountService {
    override suspend fun login(
        username: String,
        password: String,
    ): Boolean = true
}
