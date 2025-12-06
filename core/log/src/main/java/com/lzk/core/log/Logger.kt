package com.lzk.core.log

import com.tencent.mars.xlog.Log
import android.util.Log as AndroidLog

/**
 * 日志工具类
 * 封装 XLog 的使用，提供简洁的日志接口
 */
object Logger {
    // 是否使用 Android 原生 Log 输出到控制台（避免 XLog 的格式问题）
    var useAndroidLogForConsole = true
    /**
     * Verbose 级别日志
     */
    fun v(
        tag: String,
        msg: String,
    ) {
        if (useAndroidLogForConsole) {
            AndroidLog.v(tag, msg)
        }
        Log.v(tag, msg)
    }

    /**
     * Debug 级别日志
     */
    fun d(
        tag: String,
        msg: String,
    ) {
        if (useAndroidLogForConsole) {
            AndroidLog.d(tag, msg)
        }
        Log.d(tag, msg)
    }

    /**
     * Info 级别日志
     */
    fun i(
        tag: String,
        msg: String,
    ) {
        if (useAndroidLogForConsole) {
            AndroidLog.i(tag, msg)
        }
        Log.i(tag, msg)
    }

    /**
     * Warning 级别日志
     */
    fun w(
        tag: String,
        msg: String,
    ) {
        if (useAndroidLogForConsole) {
            AndroidLog.w(tag, msg)
        }
        Log.w(tag, msg)
    }

    /**
     * Error 级别日志
     */
    fun e(
        tag: String,
        msg: String,
    ) {
        if (useAndroidLogForConsole) {
            AndroidLog.e(tag, msg)
        }
        Log.e(tag, msg)
    }

    /**
     * Error 级别日志（带异常）
     */
    fun e(
        tag: String,
        msg: String,
        throwable: Throwable,
    ) {
        val fullMsg = "$msg\n${throwable.stackTraceToString()}"
        if (useAndroidLogForConsole) {
            AndroidLog.e(tag, fullMsg)
        }
        Log.e(tag, fullMsg)
    }

    /**
     * Fatal 级别日志
     */
    fun f(
        tag: String,
        msg: String,
    ) {
        if (useAndroidLogForConsole) {
            AndroidLog.e(tag, msg) // Android Log 没有 fatal 级别，使用 error
        }
        Log.f(tag, msg)
    }

    /**
     * 打印日志（带格式化参数）
     */
    fun d(
        tag: String,
        format: String,
        vararg args: Any?,
    ) {
        val msg = String.format(format, *args)
        if (useAndroidLogForConsole) {
            AndroidLog.d(tag, msg)
        }
        Log.d(tag, msg)
    }

    fun i(
        tag: String,
        format: String,
        vararg args: Any?,
    ) {
        val msg = String.format(format, *args)
        if (useAndroidLogForConsole) {
            AndroidLog.i(tag, msg)
        }
        Log.i(tag, msg)
    }

    fun w(
        tag: String,
        format: String,
        vararg args: Any?,
    ) {
        val msg = String.format(format, *args)
        if (useAndroidLogForConsole) {
            AndroidLog.w(tag, msg)
        }
        Log.w(tag, msg)
    }

    fun e(
        tag: String,
        format: String,
        vararg args: Any?,
    ) {
        val msg = String.format(format, *args)
        if (useAndroidLogForConsole) {
            AndroidLog.e(tag, msg)
        }
        Log.e(tag, msg)
    }

    /**
     * 打印方法调用栈
     */
    fun printStack(tag: String) {
        val stackTrace = Thread.currentThread().stackTrace
        val sb = StringBuilder()
        sb.append("Stack trace:\n")
        for (i in 3 until stackTrace.size) {
            sb.append("  at ${stackTrace[i]}\n")
        }
        Log.d(tag, sb.toString())
    }

    /**
     * 刷新日志
     */
    fun flush() {
        XLogConfig.flush()
    }

    /**
     * 同步刷新日志
     */
    fun flushSync() {
        XLogConfig.flushSync()
    }
}