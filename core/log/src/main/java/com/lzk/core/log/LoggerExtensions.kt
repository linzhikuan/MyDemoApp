package com.lzk.core.log

/**
 * Logger 扩展函数
 * 提供更便捷的日志使用方式
 */

/**
 * 为任意类添加日志功能
 * 使用外部传入的 TAG
 */
fun logV(
    tag: String,
    message: String,
) {
    Logger.v(tag, message)
}

fun logD(
    tag: String,
    message: String,
) {
    Logger.d(tag, message)
}

fun logI(
    tag: String,
    message: String,
) {
    Logger.i(tag, message)
}

fun logW(
    tag: String,
    message: String,
) {
    Logger.w(tag, message)
}

fun logE(
    tag: String,
    message: String,
) {
    Logger.e(tag, message)
}

fun logE(
    tag: String,
    message: String,
    throwable: Throwable,
) {
    Logger.e(tag, message, throwable)
}

/**
 * 带格式化参数的日志扩展
 */
fun logD(
    tag: String,
    format: String,
    vararg args: Any?,
) {
    Logger.d(tag, format, *args)
}

fun logI(
    tag: String,
    format: String,
    vararg args: Any?,
) {
    Logger.i(tag, format, *args)
}

fun logW(
    tag: String,
    format: String,
    vararg args: Any?,
) {
    Logger.w(tag, format, *args)
}

fun logE(
    tag: String,
    format: String,
    vararg args: Any?,
) {
    Logger.e(tag, format, *args)
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
 * 测量执行时间（需要传入 TAG）
 */
inline fun <R> measureTime(
    tag: String,
    message: String,
    block: () -> R,
): R = measureTimeWithLog(tag, message, block)

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
 * 安全执行代码块（需要传入 TAG）
 */
inline fun <R> safeExecuteWithTag(
    tag: String,
    defaultValue: R,
    block: () -> R,
): R = safeExecute(tag, defaultValue, block)
