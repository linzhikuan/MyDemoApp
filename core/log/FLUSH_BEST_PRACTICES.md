# XLog Flush 最佳实践

## 何时应该 Flush？

### ✅ 推荐 Flush 的时机

| 时机 | Flush 类型 | 重要性 | 说明 |
|------|-----------|--------|------|
| **应用进入后台** | `flush()` | ⭐⭐⭐⭐⭐ | 最重要！确保用户离开应用时日志已保存 |
| **应用崩溃** | `flushSync()` | ⭐⭐⭐⭐⭐ | 必须同步刷新，否则崩溃日志可能丢失 |
| **关键业务完成** | `flush()` | ⭐⭐⭐⭐ | 支付、登录、重要操作完成后 |
| **发生异常** | `flush()` | ⭐⭐⭐⭐ | 捕获到异常后立即刷新 |
| **应用退出** | `flushSync()` | ⭐⭐⭐⭐ | 应用正常退出时同步刷新 |
| **定时刷新** | `flush()` | ⭐⭐⭐ | 可选，适合长时间运行的应用 |
| **页面切换** | ❌ 不推荐 | ⭐ | 太频繁，影响性能 |

### ❌ 不推荐 Flush 的时机

- ❌ 每次打印日志后
- ❌ 每个页面的 onPause/onResume
- ❌ 循环中
- ❌ 高频回调中（如滚动监听）

## 完整实现示例

### 方案一：基于应用生命周期（推荐）

```kotlin
package com.lzk.mydemoapp

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.lzk.core.log.Logger
import com.lzk.core.log.XLogConfig

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // 初始化 XLog
        XLogConfig.init(this, BuildConfig.DEBUG)

        // 设置生命周期监听
        setupLifecycleCallbacks()

        // 设置崩溃处理
        setupCrashHandler()

        Logger.i("Application", "应用启动")
    }

    private fun setupLifecycleCallbacks() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            private var activityCount = 0
            private var isAppInForeground = false

            override fun onActivityStarted(activity: Activity) {
                activityCount++
                if (activityCount == 1 && !isAppInForeground) {
                    // 应用从后台回到前台
                    isAppInForeground = true
                    Logger.i("AppLifecycle", "应用进入前台")
                }
            }

            override fun onActivityStopped(activity: Activity) {
                activityCount--
                if (activityCount == 0 && isAppInForeground) {
                    // 应用进入后台 - 关键时机！
                    isAppInForeground = false
                    Logger.i("AppLifecycle", "应用进入后台")

                    // ✅ 在这里 flush，确保日志保存
                    Logger.flush()
                }
            }

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        })
    }

    private fun setupCrashHandler() {
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            try {
                Logger.e("CrashHandler", "应用崩溃: ${throwable.message}", throwable)

                // ✅ 崩溃时必须同步 flush
                Logger.flushSync()

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                defaultHandler?.uncaughtException(thread, throwable)
            }
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        Logger.i("Application", "应用终止")

        // ✅ 应用退出时同步 flush
        Logger.flushSync()
        XLogConfig.close()
    }
}
```

### 方案二：使用 ProcessLifecycleOwner（更简洁）

```kotlin
package com.lzk.mydemoapp

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.lzk.core.log.Logger
import com.lzk.core.log.XLogConfig

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        XLogConfig.init(this, BuildConfig.DEBUG)

        // 使用 ProcessLifecycleOwner 监听应用生命周期
        ProcessLifecycleOwner.get().lifecycle.addObserver(AppLifecycleObserver())

        setupCrashHandler()
    }

    private fun setupCrashHandler() {
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Logger.e("CrashHandler", "应用崩溃", throwable)
            Logger.flushSync()
            defaultHandler?.uncaughtException(thread, throwable)
        }
    }
}

class AppLifecycleObserver : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onEnterForeground() {
        Logger.i("AppLifecycle", "应用进入前台")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onEnterBackground() {
        Logger.i("AppLifecycle", "应用进入后台")

        // ✅ 应用进入后台时 flush
        Logger.flush()
    }
}
```

### 方案三：关键业务场景

```kotlin
// 1. 支付场景
class PaymentManager {

    suspend fun processPayment(amount: Double): Result<PaymentResult> {
        return try {
            Logger.i(TAG, "开始支付: amount=$amount")

            val result = paymentApi.pay(amount)

            Logger.i(TAG, "支付成功: orderId=${result.orderId}")

            // ✅ 关键业务完成后 flush
            Logger.flush()

            Result.success(result)

        } catch (e: Exception) {
            Logger.e(TAG, "支付失败", e)

            // ✅ 发生错误时也要 flush
            Logger.flush()

            Result.failure(e)
        }
    }

    companion object {
        private const val TAG = "PaymentManager"
    }
}

// 2. 登录场景
class AuthManager {

    suspend fun login(username: String, password: String): Result<User> {
        return try {
            Logger.i(TAG, "用户登录: username=$username")

            val user = authApi.login(username, password)

            Logger.i(TAG, "登录成功: userId=${user.id}")

            // ✅ 登录成功后 flush
            Logger.flush()

            Result.success(user)

        } catch (e: Exception) {
            Logger.e(TAG, "登录失败", e)
            Logger.flush()
            Result.failure(e)
        }
    }

    companion object {
        private const val TAG = "AuthManager"
    }
}

// 3. 数据同步场景
class DataSyncManager {

    suspend fun syncData() {
        try {
            Logger.i(TAG, "开始数据同步")

            val result = syncApi.sync()

            Logger.i(TAG, "数据同步完成: synced=${result.count} items")

            // ✅ 同步完成后 flush
            Logger.flush()

        } catch (e: Exception) {
            Logger.e(TAG, "数据同步失败", e)
            Logger.flush()
        }
    }

    companion object {
        private const val TAG = "DataSyncManager"
    }
}
```

### 方案四：定时自动 Flush（可选）

```kotlin
package com.lzk.mydemoapp

import android.app.Application
import android.os.Handler
import android.os.Looper
import com.lzk.core.log.Logger
import com.lzk.core.log.XLogConfig

class MyApplication : Application() {

    private val flushHandler = Handler(Looper.getMainLooper())
    private var isFlushScheduled = false

    // 定时 flush 间隔（毫秒）
    private val FLUSH_INTERVAL = 30_000L // 30 秒

    override fun onCreate() {
        super.onCreate()

        XLogConfig.init(this, BuildConfig.DEBUG)

        // 启动定时 flush（适合长时间运行的应用）
        schedulePeriodicFlush()
    }

    private fun schedulePeriodicFlush() {
        if (isFlushScheduled) return

        isFlushScheduled = true

        val flushRunnable = object : Runnable {
            override fun run() {
                Logger.flush()
                Logger.d("AutoFlush", "定时 flush 执行")

                // 继续调度下一次
                flushHandler.postDelayed(this, FLUSH_INTERVAL)
            }
        }

        flushHandler.postDelayed(flushRunnable, FLUSH_INTERVAL)
    }

    override fun onTerminate() {
        super.onTerminate()
        flushHandler.removeCallbacksAndMessages(null)
        Logger.flushSync()
        XLogConfig.close()
    }
}
```

## 性能考虑

### Flush 的性能影响

```kotlin
// ❌ 错误示例：过度 flush
fun badExample() {
    for (i in 0..1000) {
        Logger.d(TAG, "Processing item $i")
        Logger.flush() // ❌ 每次都 flush，严重影响性能
    }
}

// ✅ 正确示例：合理 flush
fun goodExample() {
    Logger.d(TAG, "开始处理 1000 个项目")

    for (i in 0..1000) {
        // 处理逻辑
        if (i % 100 == 0) {
            Logger.d(TAG, "已处理 $i 个项目")
        }
    }

    Logger.d(TAG, "处理完成")
    Logger.flush() // ✅ 处理完成后 flush 一次
}
```

### Flush vs FlushSync 的选择

```kotlin
// 异步 flush - 不阻塞当前线程
Logger.flush()

// 同步 flush - 阻塞当前线程直到写入完成
Logger.flushSync()
```

**使用建议：**

| 场景 | 使用方法 | 原因 |
|------|---------|------|
| 应用进入后台 | `flush()` | 异步即可，不影响用户体验 |
| 应用崩溃 | `flushSync()` | 必须同步，确保日志写入 |
| 应用退出 | `flushSync()` | 同步确保日志完整 |
| 关键业务完成 | `flush()` | 异步即可 |
| 普通场景 | `flush()` | 异步性能更好 |

## 总结

### 最简单的方案（推荐新手）

```kotlin
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        XLogConfig.init(this, BuildConfig.DEBUG)

        // 1. 监听应用前后台切换
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            private var activityCount = 0

            override fun onActivityStarted(activity: Activity) {
                activityCount++
            }

            override fun onActivityStopped(activity: Activity) {
                activityCount--
                if (activityCount == 0) {
                    Logger.flush() // ✅ 进入后台时 flush
                }
            }

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        })

        // 2. 设置崩溃处理
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Logger.e("Crash", "应用崩溃", throwable)
            Logger.flushSync() // ✅ 崩溃时同步 flush
            Thread.getDefaultUncaughtExceptionHandler()?.uncaughtException(thread, throwable)
        }
    }
}
```

### 核心原则

1. **应用进入后台必须 flush** - 这是最重要的时机
2. **崩溃时必须同步 flush** - 确保崩溃日志不丢失
3. **关键业务完成后 flush** - 支付、登录等重要操作
4. **不要过度 flush** - 避免在循环、高频回调中 flush
5. **优先使用异步 flush** - 除非必须同步（崩溃、退出）

遵循这些原则，你的日志系统将既可靠又高效！
