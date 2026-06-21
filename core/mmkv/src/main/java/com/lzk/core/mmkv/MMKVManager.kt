package com.lzk.core.mmkv

import android.content.Context
import com.tencent.mmkv.MMKV

/**
 * MMKV管理类
 * 提供MMKV的初始化和基础操作
 */
object MMKVManager {
    /**
     * 初始化MMKV
     * 建议在Application的onCreate中调用
     */
    fun initialize(context: Context) {
        MMKV.initialize(context)
    }

    /**
     * 获取默认的MMKV实例
     */
    fun defaultMMKV(): MMKV = MMKV.defaultMMKV()

    /**
     * 获取指定ID的MMKV实例
     * @param mmapID MMKV实例的唯一标识
     * @param mode 模式，默认为单进程模式
     */
    fun mmkvWithID(
        mmapID: String,
        mode: Int = MMKV.SINGLE_PROCESS_MODE,
    ): MMKV = MMKV.mmkvWithID(mmapID, mode)

    /**
     * 获取加密的MMKV实例
     * @param mmapID MMKV实例的唯一标识
     * @param cryptKey 加密密钥
     */
    fun mmkvWithIDAndCryptKey(
        mmapID: String,
        cryptKey: String,
    ): MMKV = MMKV.mmkvWithID(mmapID, MMKV.SINGLE_PROCESS_MODE, cryptKey)
}
