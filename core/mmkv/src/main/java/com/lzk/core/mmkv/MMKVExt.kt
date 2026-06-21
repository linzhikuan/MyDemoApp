package com.lzk.core.mmkv

import com.tencent.mmkv.MMKV

/**
 * MMKV扩展函数Ï
 */

// ==================== 基础类型存储 ====================

/**Ï
 * 存储String
 */
fun MMKV.putString(
    key: String,
    value: String?,
) = encode(key, value)

/**
 * 获取String
 */
fun MMKV.getString(
    key: String,
    defaultValue: String? = null,
): String? = decodeString(key, defaultValue)

/**
 * 存储Int
 */
fun MMKV.putInt(
    key: String,
    value: Int,
) = encode(key, value)

/**
 * 获取Int
 */
fun MMKV.getInt(
    key: String,
    defaultValue: Int = 0,
): Int = decodeInt(key, defaultValue)

/**
 * 存储Long
 */
fun MMKV.putLong(
    key: String,
    value: Long,
) = encode(key, value)

/**
 * 获取Long
 */
fun MMKV.getLong(
    key: String,
    defaultValue: Long = 0L,
): Long = decodeLong(key, defaultValue)

/**
 * 存储Float
 */
fun MMKV.putFloat(
    key: String,
    value: Float,
) = encode(key, value)

/**
 * 获取Float
 */
fun MMKV.getFloat(
    key: String,
    defaultValue: Float = 0f,
): Float = decodeFloat(key, defaultValue)

/**
 * 存储Double
 */
fun MMKV.putDouble(
    key: String,
    value: Double,
) = encode(key, value)

/**
 * 获取Double
 */
fun MMKV.getDouble(
    key: String,
    defaultValue: Double = 0.0,
): Double = decodeDouble(key, defaultValue)

/**
 * 存储Boolean
 */
fun MMKV.putBoolean(
    key: String,
    value: Boolean,
) = encode(key, value)

/**
 * 获取Boolean
 */
fun MMKV.getBoolean(
    key: String,
    defaultValue: Boolean = false,
): Boolean = decodeBool(key, defaultValue)

/**
 * 存储ByteArray
 */
fun MMKV.putBytes(
    key: String,
    value: ByteArray?,
) = encode(key, value)

/**
 * 获取ByteArray
 */
fun MMKV.getBytes(
    key: String,
    defaultValue: ByteArray? = null,
): ByteArray? = decodeBytes(key, defaultValue)

/**
 * 存储Set<String>
 */
fun MMKV.putStringSet(
    key: String,
    value: Set<String>?,
) = encode(key, value)

/**
 * 获取Set<String>
 */
fun MMKV.getStringSet(
    key: String,
    defaultValue: Set<String>? = null,
): Set<String>? = decodeStringSet(key, defaultValue)

// ==================== 对象存储（需要序列化） ====================

/**
 * 存储可序列化对象（使用Parcelable）
 */
inline fun <reified T : android.os.Parcelable> MMKV.putParcelable(
    key: String,
    value: T?,
) {
    encode(key, value)
}

/**
 * 获取可序列化对象（使用Parcelable）
 */
inline fun <reified T : android.os.Parcelable> MMKV.getParcelable(key: String): T? = decodeParcelable(key, T::class.java)

// ==================== 便捷操作 ====================

/**
 * 检查key是否存在
 */
fun MMKV.contains(key: String): Boolean = containsKey(key)

/**
 * 删除指定key
 */
fun MMKV.delete(key: String) = removeValueForKey(key)

/**
 * 删除多个key
 */
fun MMKV.delete(keys: Array<String>) = removeValuesForKeys(keys)

/**
 * 清空所有数据
 */
fun MMKV.deleteAll() = clearAll()

/**
 * 获取所有key
 */
fun MMKV.allKeys(): Array<String>? = allKeys()

/**
 * 获取数据总数
 */
fun MMKV.count(): Long = count()

/**
 * 获取占用空间大小（字节）
 */
fun MMKV.totalSize(): Long = totalSize()

/**
 * 同步数据到文件
 */
fun MMKV.sync() = sync()

/**
 * 异步数据到文件
 */
fun MMKV.async() = async()
