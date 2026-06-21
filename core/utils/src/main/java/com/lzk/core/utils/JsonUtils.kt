@file:Suppress("ktlint:standard:filename")

package com.lzk.core.utils

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * JSON工具类
 * 提供基于org.json的JSON操作便捷方法
 */
object JsonUtils {
    /**
     * 创建一个空的JSONObject
     */
    fun createJsonObject(): JSONObject = JSONObject()

    /**
     * 创建一个空的JSONArray
     */
    fun createJsonArray(): JSONArray = JSONArray()

    /**
     * 字符串转JSONObject
     *
     * @param json JSON字符串
     * @return JSONObject对象，转换失败返回null
     */
    fun parseObject(json: String?): JSONObject? {
        return try {
            if (json.isNullOrEmpty()) return null
            JSONObject(json)
        } catch (e: JSONException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 字符串转JSONArray
     *
     * @param json JSON字符串
     * @return JSONArray对象，转换失败返回null
     */
    fun parseArray(json: String?): JSONArray? {
        return try {
            if (json.isNullOrEmpty()) return null
            JSONArray(json)
        } catch (e: JSONException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Map转JSONObject
     *
     * @param map Map对象
     * @return JSONObject对象，转换失败返回null
     */
    fun mapToJsonObject(map: Map<String, Any?>?): JSONObject? {
        return try {
            if (map == null) return null
            JSONObject(map)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * List转JSONArray
     *
     * @param list List对象
     * @return JSONArray对象，转换失败返回null
     */
    fun listToJsonArray(list: List<Any?>?): JSONArray? {
        return try {
            if (list == null) return null
            JSONArray(list)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * JSONObject转Map
     *
     * @param jsonObject JSONObject对象
     * @return Map对象，转换失败返回null
     */
    fun jsonObjectToMap(jsonObject: JSONObject?): Map<String, Any?>? {
        return try {
            if (jsonObject == null) return null
            val map = mutableMapOf<String, Any?>()
            val keys = jsonObject.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                map[key] = jsonObject.get(key)
            }
            map
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * JSONArray转List
     *
     * @param jsonArray JSONArray对象
     * @return List对象，转换失败返回null
     */
    fun jsonArrayToList(jsonArray: JSONArray?): List<Any?>? {
        return try {
            if (jsonArray == null) return null
            val list = mutableListOf<Any?>()
            for (i in 0 until jsonArray.length()) {
                list.add(jsonArray.get(i))
            }
            list
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 从JSONObject中安全获取字符串
     *
     * @param jsonObject JSONObject对象
     * @param key 键名
     * @param defaultValue 默认值
     * @return 获取到的字符串值，如果不存在或类型不匹配则返回默认值
     */
    fun getString(
        jsonObject: JSONObject?,
        key: String,
        defaultValue: String = "",
    ): String {
        return try {
            if (jsonObject == null || !jsonObject.has(key)) return defaultValue
            jsonObject.optString(key, defaultValue)
        } catch (e: Exception) {
            defaultValue
        }
    }

    /**
     * 从JSONObject中安全获取整数
     *
     * @param jsonObject JSONObject对象
     * @param key 键名
     * @param defaultValue 默认值
     * @return 获取到的整数值，如果不存在或类型不匹配则返回默认值
     */
    fun getInt(
        jsonObject: JSONObject?,
        key: String,
        defaultValue: Int = 0,
    ): Int {
        return try {
            if (jsonObject == null || !jsonObject.has(key)) return defaultValue
            jsonObject.optInt(key, defaultValue)
        } catch (e: Exception) {
            defaultValue
        }
    }

    /**
     * 从JSONObject中安全获取长整数
     *
     * @param jsonObject JSONObject对象
     * @param key 键名
     * @param defaultValue 默认值
     * @return 获取到的长整数值，如果不存在或类型不匹配则返回默认值
     */
    fun getLong(
        jsonObject: JSONObject?,
        key: String,
        defaultValue: Long = 0L,
    ): Long {
        return try {
            if (jsonObject == null || !jsonObject.has(key)) return defaultValue
            jsonObject.optLong(key, defaultValue)
        } catch (e: Exception) {
            defaultValue
        }
    }

    /**
     * 从JSONObject中安全获取双精度浮点数
     *
     * @param jsonObject JSONObject对象
     * @param key 键名
     * @param defaultValue 默认值
     * @return 获取到的双精度浮点数值，如果不存在或类型不匹配则返回默认值
     */
    fun getDouble(
        jsonObject: JSONObject?,
        key: String,
        defaultValue: Double = 0.0,
    ): Double {
        return try {
            if (jsonObject == null || !jsonObject.has(key)) return defaultValue
            jsonObject.optDouble(key, defaultValue)
        } catch (e: Exception) {
            defaultValue
        }
    }

    /**
     * 从JSONObject中安全获取布尔值
     *
     * @param jsonObject JSONObject对象
     * @param key 键名
     * @param defaultValue 默认值
     * @return 获取到的布尔值，如果不存在或类型不匹配则返回默认值
     */
    fun getBoolean(
        jsonObject: JSONObject?,
        key: String,
        defaultValue: Boolean = false,
    ): Boolean {
        return try {
            if (jsonObject == null || !jsonObject.has(key)) return defaultValue
            jsonObject.optBoolean(key, defaultValue)
        } catch (e: Exception) {
            defaultValue
        }
    }

    /**
     * 从JSONObject中安全获取JSONObject
     *
     * @param jsonObject JSONObject对象
     * @param key 键名
     * @return 获取到的JSONObject，如果不存在或类型不匹配则返回null
     */
    fun getJsonObject(
        jsonObject: JSONObject?,
        key: String,
    ): JSONObject? {
        return try {
            if (jsonObject == null || !jsonObject.has(key)) return null
            jsonObject.optJSONObject(key)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 从JSONObject中安全获取JSONArray
     *
     * @param jsonObject JSONObject对象
     * @param key 键名
     * @return 获取到的JSONArray，如果不存在或类型不匹配则返回null
     */
    fun getJsonArray(
        jsonObject: JSONObject?,
        key: String,
    ): JSONArray? {
        return try {
            if (jsonObject == null || !jsonObject.has(key)) return null
            jsonObject.optJSONArray(key)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 判断字符串是否为有效的JSON
     *
     * @param json 要判断的字符串
     * @return true表示是有效的JSON，false表示不是
     */
    fun isValidJson(json: String?): Boolean {
        if (json.isNullOrEmpty()) return false
        return try {
            JSONObject(json)
            true
        } catch (e: JSONException) {
            try {
                JSONArray(json)
                true
            } catch (e: JSONException) {
                false
            }
        }
    }

    /**
     * 判断字符串是否为有效的JSON对象
     *
     * @param json 要判断的字符串
     * @return true表示是有效的JSON对象，false表示不是
     */
    fun isValidJsonObject(json: String?): Boolean {
        return try {
            if (json.isNullOrEmpty()) return false
            JSONObject(json)
            true
        } catch (e: JSONException) {
            false
        }
    }

    /**
     * 判断字符串是否为有效的JSON数组
     *
     * @param json 要判断的字符串
     * @return true表示是有效的JSON数组，false表示不是
     */
    fun isValidJsonArray(json: String?): Boolean {
        return try {
            if (json.isNullOrEmpty()) return false
            JSONArray(json)
            true
        } catch (e: JSONException) {
            false
        }
    }

    /**
     * 美化JSON字符串
     *
     * @param json JSON字符串
     * @param indent 缩进空格数
     * @return 美化后的JSON字符串，失败返回原字符串
     */
    fun formatJson(
        json: String?,
        indent: Int = 4,
    ): String? {
        if (json.isNullOrEmpty()) return json
        return try {
            // 尝试作为JSONObject解析
            val jsonObject = JSONObject(json)
            jsonObject.toString(indent)
        } catch (e: JSONException) {
            try {
                // 尝试作为JSONArray解析
                val jsonArray = JSONArray(json)
                jsonArray.toString(indent)
            } catch (e: JSONException) {
                json
            }
        }
    }

    /**
     * 合并两个JSONObject
     *
     * @param target 目标JSONObject（会被修改）
     * @param source 源JSONObject
     * @return 合并后的JSONObject
     */
    fun mergeJsonObjects(
        target: JSONObject,
        source: JSONObject,
    ): JSONObject {
        try {
            val keys = source.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                val value = source.get(key)

                if (value is JSONObject && target.has(key)) {
                    val targetValue = target.get(key)
                    if (targetValue is JSONObject) {
                        // 递归合并嵌套的JSONObject
                        mergeJsonObjects(targetValue, value)
                        continue
                    }
                }
                target.put(key, value)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return target
    }

    /**
     * 从JSONArray中获取字符串列表
     *
     * @param jsonArray JSONArray对象
     * @return 字符串列表，转换失败返回空列表
     */
    fun getStringList(jsonArray: JSONArray?): List<String> {
        val list = mutableListOf<String>()
        if (jsonArray == null) return list

        try {
            for (i in 0 until jsonArray.length()) {
                list.add(jsonArray.optString(i, ""))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    /**
     * 从JSONArray中获取整数列表
     *
     * @param jsonArray JSONArray对象
     * @return 整数列表，转换失败返回空列表
     */
    fun getIntList(jsonArray: JSONArray?): List<Int> {
        val list = mutableListOf<Int>()
        if (jsonArray == null) return list

        try {
            for (i in 0 until jsonArray.length()) {
                list.add(jsonArray.optInt(i, 0))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    /**
     * 从JSONArray中获取长整数列表
     *
     * @param jsonArray JSONArray对象
     * @return 长整数列表，转换失败返回空列表
     */
    fun getLongList(jsonArray: JSONArray?): List<Long> {
        val list = mutableListOf<Long>()
        if (jsonArray == null) return list

        try {
            for (i in 0 until jsonArray.length()) {
                list.add(jsonArray.optLong(i, 0L))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    /**
     * 从JSONArray中获取双精度浮点数列表
     *
     * @param jsonArray JSONArray对象
     * @return 双精度浮点数列表，转换失败返回空列表
     */
    fun getDoubleList(jsonArray: JSONArray?): List<Double> {
        val list = mutableListOf<Double>()
        if (jsonArray == null) return list

        try {
            for (i in 0 until jsonArray.length()) {
                list.add(jsonArray.optDouble(i, 0.0))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    /**
     * 从JSONArray中获取布尔值列表
     *
     * @param jsonArray JSONArray对象
     * @return 布尔值列表，转换失败返回空列表
     */
    fun getBooleanList(jsonArray: JSONArray?): List<Boolean> {
        val list = mutableListOf<Boolean>()
        if (jsonArray == null) return list

        try {
            for (i in 0 until jsonArray.length()) {
                list.add(jsonArray.optBoolean(i, false))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    /**
     * 移除JSONObject中的null值
     *
     * @param jsonObject JSONObject对象
     * @return 移除null值后的JSONObject
     */
    fun removeNullValues(jsonObject: JSONObject): JSONObject {
        try {
            val keys = jsonObject.keys()
            val keysToRemove = mutableListOf<String>()

            while (keys.hasNext()) {
                val key = keys.next()
                if (jsonObject.isNull(key)) {
                    keysToRemove.add(key)
                } else {
                    val value = jsonObject.get(key)
                    if (value is JSONObject) {
                        removeNullValues(value)
                    }
                }
            }

            keysToRemove.forEach { jsonObject.remove(it) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return jsonObject
    }

    /**
     * 将Gson的JsonObject转换为org.json的JSONObject
     *
     * @param gsonObject Gson的JsonObject
     * @return org.json的JSONObject，转换失败返回null
     */
    fun gsonToJsonObject(gsonObject: JsonObject?): JSONObject? {
        return try {
            if (gsonObject == null) return null
            JSONObject(gsonObject.toString())
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 将Gson的JsonArray转换为org.json的JSONArray
     *
     * @param gsonArray Gson的JsonArray
     * @return org.json的JSONArray，转换失败返回null
     */
    fun gsonToJsonArray(gsonArray: JsonArray?): JSONArray? {
        return try {
            if (gsonArray == null) return null
            JSONArray(gsonArray.toString())
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 将org.json的JSONObject转换为Gson的JsonObject
     *
     * @param jsonObject org.json的JSONObject
     * @return Gson的JsonObject，转换失败返回null
     */
    fun jsonObjectToGson(jsonObject: JSONObject?): JsonObject? {
        return try {
            if (jsonObject == null) return null
            GsonUtils.toJsonObject(jsonObject.toString())
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 将org.json的JSONArray转换为Gson的JsonArray
     *
     * @param jsonArray org.json的JSONArray
     * @return Gson的JsonArray，转换失败返回null
     */
    fun jsonArrayToGson(jsonArray: JSONArray?): JsonArray? {
        return try {
            if (jsonArray == null) return null
            GsonUtils.toJsonArray(jsonArray.toString())
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
