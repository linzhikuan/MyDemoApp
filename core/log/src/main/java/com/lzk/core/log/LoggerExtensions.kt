package com.lzk.core.log

/**
 * Logger 扩展函数
 * 提供更便捷的日志使用方式
 */

/**
 * 为任意类添加日志功能
 * 使用类名作为 TAG
 */
inline fun <reified T> T.logV(message: String) {
    Logger.v(T::class.java.simpleName, message)
}

inline fun <reified T> T.logD(message: String) {
    Logger.d(T::class.java.simpleName, message)
}

inline fun <reified T> T.logI(message: String) {
    Logger.i(T::class.java.simpleName, message)
}

inline fun <reified T> T.logW(message: String) {
    Logger.w(T::class.java.simpleName, message)
}

inline fun <reified T> T.logE(message: String) {
    Logger.e(T::class.java.simpleName, message)
}

inline fun <reified T> T.logE(
    message: String,
    throwable: Throwable,
) {
    Logger.e(T::class.java.simpleName, message, throwable)
}

/**
 * 带格式化参数的日志扩展
 */
inline fun <reified T> T.logD(
    format: String,
    vararg args: Any?,
) {
    Logger.d(T::class.java.simpleName, format, *args)
}

inline fun <reified T> T.logI(
    format: String,
    vararg args: Any?,
) {
    Logger.i(T::class.java.simpleName, format, *args)
}

inline fun <reified T> T.logW(
    format: String,
    vararg args: Any?,
) {
    Logger.w(T::class.java.simpleName, format, *args)
}

inline fun <reified T> T.logE(
    format: String,
    vararg args: Any?,
) {
    Logger.e(T::class.java.simpleName, format, *args)
}

/**
 * 测量代码执行时间并打印日志
 */
inline fun <T> measureTimeWithLog(
    tag: String,
    message: String,
    block: () -> T,
): T {
    val startTime = System.currentTimeMillis()
    val result = block()
    val duration = System.currentTimeMillis() - startTime
    Logger.d(tag, "$message took ${duration}ms")
    return result
}

/**
 * 使用类名作为 TAG 测量执行时间
 */
inline fun <reified T, R> T.measureTime(
    message: String,
    block: () -> R,
): R = measureTimeWithLog(T::class.java.simpleName, message, block)

/**
 * 安全执行代码块，捕获异常并记录日志
 */
inline fun <T> safeExecute(
    tag: String,
    defaultValue: T,
    block: () -> T,
): T =
    try {
        block()
    } catch (e: Exception) {
        Logger.e(tag, "Exception in safeExecute", e)
        defaultValue
    }

/**
 * 使用类名作为 TAG 安全执行代码块
 */
inline fun <reified T, R> T.safeExecute(
    defaultValue: R,
    block: () -> R,
): R = safeExecute(T::class.java.simpleName, defaultValue, block)
