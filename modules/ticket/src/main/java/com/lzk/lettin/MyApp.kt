package com.lzk.lettin

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.lzk.core.log.XLogConfig
import com.lzk.core.log.logI
import com.lzk.core.mmkv.MMKVManager
import com.lzk.core.utils.AppUtil
import com.lzk.core.utils.launch
import com.lzk.lettin.business.main.data.repository.LotteryRepository
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyApp : Application() {
    companion object {
        private const val TAG = "MyApp"
    }

    @Inject
    lateinit var lotteryRepository: LotteryRepository

    override fun onCreate() {
        super.onCreate()
        AppUtil.init(this)
        XLogConfig.init(this, BuildConfig.isDebug)
        if (BuildConfig.isDebug) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(this)
        MMKVManager.initialize(this)

        // 保证首次启动就有 mock 数据
        launch {
            runCatching {
                lotteryRepository.ensureMockDataForAll()
            }.onFailure {
                logI(TAG, "ensureMockDataForAll 失败: ${it.message}")
            }
        }

        launch {
            AppUtil.isAppInForeground.collect {
                logI(TAG, "App is ${if (it) "in" else "out"} foreground")
                if (!it) {
                    XLogConfig.flushSync()
                }
            }
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        XLogConfig.close()
    }
}
