package com.lzk.core.log

import android.content.Context
import com.tencent.mars.xlog.Log
import com.tencent.mars.xlog.Xlog
import java.io.File

/**
 * XLog 配置类
 * 用于初始化和配置 Tencent Mars XLog
 */
object XLogConfig {
    private const val LOG_DIR_NAME = "xlog"
    private const val CACHE_DIR_NAME = "xlog_cache"
    private const val LOG_PREFIX = "MyDemoApp"

    private var isInitialized = false

    /**
     * 初始化 XLog
     * @param context 应用上下文
     * @param isDebug 是否为调试模式
     * @param logLevel 日志级别，默认为 DEBUG
     */
    fun init(
        context: Context,
        isDebug: Boolean = true,
        logLevel: Int = if (isDebug) Xlog.LEVEL_DEBUG else Xlog.LEVEL_INFO,
    ) {
        if (isInitialized) {
            return
        }

        val logDir = getLogDir(context)
        val cacheDir = getCacheDir(context)

        // 确保目录存在
        if (!logDir.exists()) {
            logDir.mkdirs()
        }
        if (!cacheDir.exists()) {
            cacheDir.mkdirs()
        }

        // 配置 XLog
        System.loadLibrary("c++_shared")
        System.loadLibrary("marsxlog")

        val xlog = Xlog()
        Log.setLogImp(xlog)

        // 设置日志配置
        Log.appenderOpen(
            logLevel, // 日志级别
            Xlog.AppednerModeAsync, // 异步模式
            cacheDir.absolutePath, // 缓存目录
            logDir.absolutePath, // 日志目录
            LOG_PREFIX, // 日志文件名前缀
            7 // 缓存天数
        )

        // 关闭 XLog 的控制台输出，避免 [, , 0] 格式问题
        // 使用 Android 原生 Log 输出到控制台
        Log.setConsoleLogOpen(false)

        isInitialized = true

        Logger.i("XLog", "XLog initialized successfully")
        Logger.i("XLog", "Log directory: ${logDir.absolutePath}")
        Logger.i("XLog", "Cache directory: ${cacheDir.absolutePath}")
    }

    /**
     * 获取日志目录
     */
    fun getLogDir(context: Context): File = File(context.getExternalFilesDir(null), LOG_DIR_NAME)

    /**
     * 获取缓存目录
     */
    fun getCacheDir(context: Context): File = File(context.filesDir, CACHE_DIR_NAME)

    /**
     * 刷新日志到文件
     * 建议在应用退出或进入后台时调用
     */
    fun flush() {
        if (isInitialized) {
            Log.appenderFlush()
        }
    }

    /**
     * 同步刷新日志到文件
     */
    fun flushSync() {
        if (isInitialized) {
            Log.appenderFlush()
        }
    }

    /**
     * 关闭 XLog
     * 建议在应用退出时调用
     */
    fun close() {
        if (isInitialized) {
            Log.appenderClose()
            isInitialized = false
        }
    }

    /**
     * 清理过期日志
     * @param context 应用上下文
     * @param daysToKeep 保留最近几天的日志，默认7天
     */
    fun cleanOldLogs(
        context: Context,
        daysToKeep: Int = 7,
    ) {
        val logDir = getLogDir(context)
        if (!logDir.exists()) {
            return
        }

        val currentTime = System.currentTimeMillis()
        val keepDuration = daysToKeep * 24 * 60 * 60 * 1000L

        logDir.listFiles()?.forEach { file ->
            if (file.isFile && currentTime - file.lastModified() > keepDuration) {
                file.delete()
                Logger.d("XLog", "Deleted old log file: ${file.name}")
            }
        }
    }
}
