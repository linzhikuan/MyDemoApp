package com.lzk.lettin

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.lzk.core.log.XLogConfig
import com.lzk.core.log.logI
import com.lzk.core.utils.AppUtil
import com.lzk.core.utils.launch
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application() {
    companion object {
        private const val TAG = "MyApp"
    }

    override fun onCreate() {
        super.onCreate()
        AppUtil.init(this)
        // 初始化XLog
        XLogConfig.init(this, BuildConfig.isDebug)
        // 初始化ARouter
        if (BuildConfig.isDebug) {
            ARouter.openLog() // 开启日志
            ARouter.openDebug() // 开启调试模式
        }
        ARouter.init(this)

        launch {
            AppUtil.crashState.collect {
                if (it != null) {
                    logI(TAG, "Crash occurred: ${it.message}")
                    XLogConfig.flushSync()
                }
            }
        }

        launch {
            AppUtil.isAppInForeground.collect {
                logI(TAG, "App is ${if (it) "in" else "out"} foreground")
                if (!it) {
                    XLogConfig.flush()
                }
            }
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        XLogConfig.close()
    }
}
