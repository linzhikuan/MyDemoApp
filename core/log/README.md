# XLog 使用指南

本模块封装了腾讯 Mars XLog，提供高性能的日志记录功能。

## 功能特点

- ✅ 高性能异步日志写入
- ✅ 支持日志文件持久化
- ✅ 自动日志压缩和加密（可选）
- ✅ 支持多种日志级别
- ✅ 自动清理过期日志
- ✅ 崩溃时日志不丢失

## 快速开始

### 1. 添加依赖

在你的模块的 `build.gradle.kts` 中添加：

```kotlin
dependencies {
    implementation(project(":core:log"))
}
```

### 2. 初始化 XLog

在 Application 的 `onCreate()` 方法中初始化：

```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // 初始化 XLog
        XLogConfig.init(
            context = this,
            isDebug = BuildConfig.DEBUG
        )
    }

    override fun onTerminate() {
        super.onTerminate()
        // 关闭 XLog
        XLogConfig.close()
    }
}
```

### 3. 使用日志

```kotlin
import com.lzk.core.log.Logger

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Verbose 日志
        Logger.v(TAG, "This is a verbose log")

        // Debug 日志
        Logger.d(TAG, "This is a debug log")

        // Info 日志
        Logger.i(TAG, "This is an info log")

        // Warning 日志
        Logger.w(TAG, "This is a warning log")

        // Error 日志
        Logger.e(TAG, "This is an error log")

        // Error 日志（带异常）
        try {
            // 可能抛出异常的代码
        } catch (e: Exception) {
            Logger.e(TAG, "An error occurred", e)
        }

        // 格式化日志
        Logger.d(TAG, "User %s logged in at %d", "John", System.currentTimeMillis())

        // 打印调用栈
        Logger.printStack(TAG)
    }
}
```

## 高级用法

### 手动刷新日志

在某些关键时刻（如应用退出、进入后台），可以手动刷新日志到文件：

```kotlin
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        XLogConfig.init(this, BuildConfig.DEBUG)

        // 监听应用生命周期
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity) {
                // 应用进入后台时刷新日志
                Logger.flush()
            }

            // 其他回调方法...
        })
    }
}
```

### 清理过期日志

定期清理过期日志，避免占用过多存储空间：

```kotlin
// 清理7天前的日志（默认）
XLogConfig.cleanOldLogs(context)

// 清理3天前的日志
XLogConfig.cleanOldLogs(context, daysToKeep = 3)
```

### 获取日志文件路径

```kotlin
// 获取日志目录
val logDir = XLogConfig.getLogDir(context)
Logger.i(TAG, "Log directory: ${logDir.absolutePath}")

// 获取缓存目录
val cacheDir = XLogConfig.getCacheDir(context)
Logger.i(TAG, "Cache directory: ${cacheDir.absolutePath}")
```

### 在崩溃处理中使用

```kotlin
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        XLogConfig.init(this, BuildConfig.DEBUG)

        // 设置全局异常处理
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Logger.e("CrashHandler", "Uncaught exception in thread ${thread.name}", throwable)
            // 同步刷新日志，确保崩溃日志被写入
            Logger.flushSync()

            // 调用系统默认的异常处理
            Thread.getDefaultUncaughtExceptionHandler()?.uncaughtException(thread, throwable)
        }
    }
}
```

## 日志级别说明

| 级别 | 方法 | 说明 | 使用场景 |
|------|------|------|----------|
| VERBOSE | `Logger.v()` | 详细信息 | 开发调试时的详细信息 |
| DEBUG | `Logger.d()` | 调试信息 | 开发调试时使用 |
| INFO | `Logger.i()` | 一般信息 | 重要的业务流程信息 |
| WARNING | `Logger.w()` | 警告信息 | 潜在的问题或异常情况 |
| ERROR | `Logger.e()` | 错误信息 | 错误和异常 |
| FATAL | `Logger.f()` | 致命错误 | 严重错误，可能导致应用崩溃 |

## 最佳实践

### 1. 使用常量定义 TAG

```kotlin
class MyClass {
    companion object {
        private const val TAG = "MyClass"
    }

    fun doSomething() {
        Logger.d(TAG, "Doing something...")
    }
}
```

### 2. 在关键位置刷新日志

```kotlin
// 应用退出时
override fun onDestroy() {
    super.onDestroy()
    Logger.flushSync()
}

// 重要操作完成后
fun importantOperation() {
    try {
        // 执行重要操作
        Logger.i(TAG, "Important operation completed")
        Logger.flush()
    } catch (e: Exception) {
        Logger.e(TAG, "Important operation failed", e)
        Logger.flushSync()
    }
}
```

### 3. 定期清理日志

```kotlin
// 在应用启动时清理过期日志
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        XLogConfig.init(this, BuildConfig.DEBUG)

        // 异步清理过期日志
        Thread {
            XLogConfig.cleanOldLogs(this, daysToKeep = 7)
        }.start()
    }
}
```

### 4. 避免在循环中频繁打印日志

```kotlin
// ❌ 不推荐
for (i in 0..10000) {
    Logger.d(TAG, "Processing item $i")
}

// ✅ 推荐
Logger.d(TAG, "Start processing 10000 items")
for (i in 0..10000) {
    // 处理逻辑
    if (i % 1000 == 0) {
        Logger.d(TAG, "Processed $i items")
    }
}
Logger.d(TAG, "Finished processing all items")
```

## 注意事项

1. **必须在 Application 中初始化**：确保在使用日志前调用 `XLogConfig.init()`
2. **权限要求**：需要存储权限来写入日志文件（Android 10+ 使用 scoped storage，无需额外权限）
3. **性能考虑**：XLog 使用异步写入，性能优秀，但仍建议避免过度打印日志
4. **日志文件位置**：日志文件保存在 `context.getExternalFilesDir(null)/xlog/` 目录下
5. **应用卸载**：应用卸载时，日志文件会被自动删除

## 故障排查

### 日志文件没有生成

1. 检查是否正确初始化了 XLog
2. 检查是否有存储权限
3. 检查日志级别设置是否正确
4. 尝试手动调用 `Logger.flush()` 刷新日志

### 日志内容不完整

1. 在应用退出前调用 `Logger.flushSync()` 同步刷新
2. 检查是否在多进程环境下使用（需要为每个进程单独初始化）

### 日志文件过大

1. 定期调用 `XLogConfig.cleanOldLogs()` 清理过期日志
2. 在生产环境中使用较高的日志级别（如 INFO 或 WARNING）
3. 避免打印大量重复或无用的日志

## 示例代码

完整的示例代码请参考项目中的示例模块。
