# XLog 使用示例

## 完整的使用示例

### 1. 在 Application 中初始化

```kotlin
package com.lzk.mydemoapp

import android.app.Application
import com.lzk.core.log.Logger
import com.lzk.core.log.XLogConfig

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // 初始化 XLog
        XLogConfig.init(
            context = this,
            isDebug = BuildConfig.DEBUG
        )

        Logger.i("Application", "Application started")

        // 设置全局异常处理
        setupCrashHandler()

        // 清理过期日志（异步执行）
        Thread {
            XLogConfig.cleanOldLogs(this, daysToKeep = 7)
        }.start()
    }

    private fun setupCrashHandler() {
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Logger.e("CrashHandler", "Uncaught exception in thread ${thread.name}", throwable)
            Logger.flushSync()
            defaultHandler?.uncaughtException(thread, throwable)
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        XLogConfig.close()
    }
}
```

### 2. 在 Activity 中使用

```kotlin
package com.lzk.mydemoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lzk.core.log.Logger
import com.lzk.core.log.logD
import com.lzk.core.log.logE
import com.lzk.core.log.logI
import com.lzk.core.log.measureTime

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 方式1: 使用 Logger 对象
        Logger.d(TAG, "onCreate called")

        // 方式2: 使用扩展函数（自动使用类名作为 TAG）
        logI("Activity created")

        // 格式化日志
        val userId = 12345
        val userName = "张三"
        Logger.i(TAG, "User logged in: id=%d, name=%s", userId, userName)

        // 测量代码执行时间
        val result = measureTime("Load data") {
            loadData()
        }

        // 异常处理
        try {
            riskyOperation()
        } catch (e: Exception) {
            logE("Operation failed", e)
        }
    }

    private fun loadData(): String {
        Thread.sleep(100)
        return "Data loaded"
    }

    private fun riskyOperation() {
        throw RuntimeException("Something went wrong")
    }

    override fun onPause() {
        super.onPause()
        // 应用进入后台时刷新日志
        Logger.flush()
    }
}
```

### 3. 在 ViewModel 中使用

```kotlin
package com.lzk.mydemoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lzk.core.log.logD
import com.lzk.core.log.logE
import com.lzk.core.log.logI
import com.lzk.core.log.safeExecute
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    fun loadUserData(userId: String) {
        logD("Loading user data for userId: %s", userId)

        viewModelScope.launch {
            // 使用 safeExecute 安全执行代码
            val result = safeExecute(defaultValue = null) {
                fetchUserFromNetwork(userId)
            }

            if (result != null) {
                logI("User data loaded successfully")
            } else {
                logE("Failed to load user data")
            }
        }
    }

    private suspend fun fetchUserFromNetwork(userId: String): User? {
        // 模拟网络请求
        return null
    }
}

data class User(val id: String, val name: String)
```

### 4. 在 Repository 中使用

```kotlin
package com.lzk.mydemoapp.repository

import com.lzk.core.log.Logger
import com.lzk.core.log.logD
import com.lzk.core.log.logE
import com.lzk.core.log.measureTime

class UserRepository {

    companion object {
        private const val TAG = "UserRepository"
    }

    suspend fun getUserById(userId: String): Result<User> {
        return try {
            logD("Fetching user: %s", userId)

            val user = measureTime("Database query") {
                // 模拟数据库查询
                queryDatabase(userId)
            }

            Logger.i(TAG, "User fetched successfully: ${user.name}")
            Result.success(user)

        } catch (e: Exception) {
            logE("Failed to fetch user", e)
            Result.failure(e)
        }
    }

    private fun queryDatabase(userId: String): User {
        Thread.sleep(50)
        return User(userId, "User $userId")
    }
}
```

### 5. 在网络请求中使用

```kotlin
package com.lzk.mydemoapp.network

import com.lzk.core.log.Logger
import okhttp3.Interceptor
import okhttp3.Response

class LoggingInterceptor : Interceptor {

    companion object {
        private const val TAG = "NetworkLog"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        Logger.d(TAG, "Request: ${request.method} ${request.url}")
        Logger.d(TAG, "Headers: ${request.headers}")

        val startTime = System.currentTimeMillis()

        return try {
            val response = chain.proceed(request)
            val duration = System.currentTimeMillis() - startTime

            Logger.i(TAG, "Response: ${response.code} ${request.url} (${duration}ms)")

            response
        } catch (e: Exception) {
            val duration = System.currentTimeMillis() - startTime
            Logger.e(TAG, "Request failed: ${request.url} (${duration}ms)", e)
            throw e
        }
    }
}
```

### 6. 查看日志文件

```kotlin
package com.lzk.mydemoapp

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.lzk.core.log.Logger
import com.lzk.core.log.XLogConfig
import java.io.File

class LogManager {

    companion object {
        private const val TAG = "LogManager"
    }

    /**
     * 获取所有日志文件
     */
    fun getLogFiles(context: Context): List<File> {
        val logDir = XLogConfig.getLogDir(context)
        return logDir.listFiles()?.filter { it.isFile }?.sortedByDescending { it.lastModified() } ?: emptyList()
    }

    /**
     * 分享日志文件
     */
    fun shareLogFile(context: Context, logFile: File) {
        try {
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                logFile
            )

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            context.startActivity(Intent.createChooser(intent, "分享日志文件"))
            Logger.i(TAG, "Sharing log file: ${logFile.name}")

        } catch (e: Exception) {
            Logger.e(TAG, "Failed to share log file", e)
        }
    }

    /**
     * 删除所有日志文件
     */
    fun clearAllLogs(context: Context) {
        val logDir = XLogConfig.getLogDir(context)
        logDir.listFiles()?.forEach { file ->
            if (file.isFile) {
                file.delete()
                Logger.d(TAG, "Deleted log file: ${file.name}")
            }
        }
    }
}
```

### 7. 在 AndroidManifest.xml 中配置 Application

```xml
<application
    android:name=".MyApplication"
    android:allowBackup="true"
    android:icon="@mipmap/ic_launcher"
    android:label="@string/app_name"
    android:theme="@style/AppTheme">

    <!-- 如果需要分享日志文件，添加 FileProvider -->
    <provider
        android:name="androidx.core.content.FileProvider"
        android:authorities="${applicationId}.fileprovider"
        android:exported="false"
        android:grantUriPermissions="true">
        <meta-data
            android:name="android.support.FILE_PROVIDER_PATHS"
            android:resource="@xml/file_paths" />
    </provider>

    <!-- 其他配置 -->
</application>
```

### 8. 创建 file_paths.xml (如果需要分享日志)

在 `res/xml/file_paths.xml`:

```xml
<?xml version="1.0" encoding="utf-8"?>
<paths>
    <external-files-path
        name="xlog"
        path="xlog/" />
</paths>
```

## 常见使用场景

### 场景1: 追踪用户操作流程

```kotlin
class CheckoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logI("Checkout process started")

        btnConfirm.setOnClickListener {
            logD("User clicked confirm button")
            processCheckout()
        }
    }

    private fun processCheckout() {
        logI("Processing checkout...")

        // 步骤1
        logD("Step 1: Validating cart")

        // 步骤2
        logD("Step 2: Processing payment")

        // 步骤3
        logD("Step 3: Creating order")

        logI("Checkout completed successfully")
    }
}
```

### 场景2: 性能监控

```kotlin
class ImageLoader {

    fun loadImage(url: String) {
        val result = measureTime("Load image from $url") {
            // 加载图片的代码
            downloadAndDecodeImage(url)
        }

        logI("Image loaded: size=${result.size}")
    }
}
```

### 场景3: 调试复杂的业务逻辑

```kotlin
class OrderProcessor {

    fun processOrder(order: Order) {
        logD("Processing order: ${order.id}")
        logD("Order details: items=${order.items.size}, total=${order.total}")

        if (order.items.isEmpty()) {
            logW("Order has no items, skipping")
            return
        }

        order.items.forEachIndexed { index, item ->
            logD("Processing item $index: ${item.name}, price=${item.price}")
        }

        logI("Order processed successfully: ${order.id}")
    }
}
```

## 注意事项

1. **同步 Gradle**: 添加依赖后，需要点击 "Sync Now" 同步 Gradle
2. **初始化时机**: 必须在 Application 的 onCreate 中初始化
3. **日志刷新**: 在应用退出或关键操作后调用 flush() 确保日志写入
4. **日志清理**: 定期清理过期日志，避免占用过多存储空间
5. **生产环境**: 在生产环境中使用较高的日志级别（INFO 或 WARNING）
