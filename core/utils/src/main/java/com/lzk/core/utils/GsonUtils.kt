@file:Suppress("ktlint:standard:filename")

package com.lzk.core.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * Gson工具类
 * 提供JSON序列化和反序列化的便捷方法
 */
object GsonUtils {
    /**
     * 默认Gson实例
     */
    private val gsonInstance: Gson by lazy {
        GsonBuilder()
            .serializeNulls() // 序列化null值
            .create()
    }

    /**
     * 美化输出的Gson实例
     */
    private val prettyGsonInstance: Gson by lazy {
        GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .create()
    }

    /**
     * 获取默认Gson实例
     */
    fun getGson(): Gson = gsonInstance

    /**
     * 获取美化输出的Gson实例
     */
    fun getPrettyGson(): Gson = prettyGsonInstance

    /**
     * 对象转JSON字符串
     *
     * @param obj 要转换的对象
     * @return JSON字符串，转换失败返回null
     */
    fun toJson(obj: Any?): String? {
        return try {
            if (obj == null) return null
            gsonInstance.toJson(obj)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 对象转美化的JSON字符串
     *
     * @param obj 要转换的对象
     * @return 美化的JSON字符串，转换失败返回null
     */
    fun toPrettyJson(obj: Any?): String? {
        return try {
            if (obj == null) return null
            prettyGsonInstance.toJson(obj)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * JSON字符串转对象
     *
     * @param json JSON字符串
     * @param clazz 目标类型
     * @return 转换后的对象，转换失败返回null
     */
    fun <T> fromJson(
        json: String?,
        clazz: Class<T>,
    ): T? {
        return try {
            if (json.isNullOrEmpty()) return null
            gsonInstance.fromJson(json, clazz)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * JSON字符串转对象（支持泛型）
     *
     * @param json JSON字符串
     * @param type 目标类型
     * @return 转换后的对象，转换失败返回null
     */
    fun <T> fromJson(
        json: String?,
        type: Type,
    ): T? {
        return try {
            if (json.isNullOrEmpty()) return null
            gsonInstance.fromJson(json, type)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * JSON字符串转对象（使用TypeToken）
     *
     * @param json JSON字符串
     * @param typeToken TypeToken对象
     * @return 转换后的对象，转换失败返回null
     */
    fun <T> fromJson(
        json: String?,
        typeToken: TypeToken<T>,
    ): T? {
        return try {
            if (json.isNullOrEmpty()) return null
            gsonInstance.fromJson(json, typeToken.type)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * JSON字符串转List
     *
     * @param json JSON字符串
     * @param clazz List元素的类型
     * @return List对象，转换失败返回null
     */
    fun <T> fromJsonToList(
        json: String?,
        clazz: Class<T>,
    ): List<T>? {
        return try {
            if (json.isNullOrEmpty()) return null
            val type = TypeToken.getParameterized(List::class.java, clazz).type
            gsonInstance.fromJson(json, type)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * JSON字符串转ArrayList
     *
     * @param json JSON字符串
     * @param clazz ArrayList元素的类型
     * @return ArrayList对象，转换失败返回null
     */
    fun <T> fromJsonToArrayList(
        json: String?,
        clazz: Class<T>,
    ): ArrayList<T>? {
        return try {
            if (json.isNullOrEmpty()) return null
            val type = TypeToken.getParameterized(ArrayList::class.java, clazz).type
            gsonInstance.fromJson(json, type)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * JSON字符串转Map
     *
     * @param json JSON字符串
     * @return Map对象，转换失败返回null
     */
    fun fromJsonToMap(json: String?): Map<String, Any>? {
        return try {
            if (json.isNullOrEmpty()) return null
            val type = object : TypeToken<Map<String, Any>>() {}.type
            gsonInstance.fromJson(json, type)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * JSON字符串转Map（指定value类型）
     *
     * @param json JSON字符串
     * @param valueClass value的类型
     * @return Map对象，转换失败返回null
     */
    fun <V> fromJsonToMap(
        json: String?,
        valueClass: Class<V>,
    ): Map<String, V>? {
        return try {
            if (json.isNullOrEmpty()) return null
            val type =
                TypeToken.getParameterized(Map::class.java, String::class.java, valueClass).type
            gsonInstance.fromJson(json, type)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * JSON字符串转JsonObject
     *
     * @param json JSON字符串
     * @return JsonObject对象，转换失败返回null
     */
    fun toJsonObject(json: String?): JsonObject? {
        return try {
            if (json.isNullOrEmpty()) return null
            JsonParser.parseString(json).asJsonObject
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * JSON字符串转JsonArray
     *
     * @param json JSON字符串
     * @return JsonArray对象，转换失败返回null
     */
    fun toJsonArray(json: String?): JsonArray? {
        return try {
            if (json.isNullOrEmpty()) return null
            JsonParser.parseString(json).asJsonArray
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * JSON字符串转JsonElement
     *
     * @param json JSON字符串
     * @return JsonElement对象，转换失败返回null
     */
    fun toJsonElement(json: String?): JsonElement? {
        return try {
            if (json.isNullOrEmpty()) return null
            JsonParser.parseString(json)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 对象转JsonObject
     *
     * @param obj 要转换的对象
     * @return JsonObject对象，转换失败返回null
     */
    fun objectToJsonObject(obj: Any?): JsonObject? {
        return try {
            if (obj == null) return null
            val json = toJson(obj) ?: return null
            toJsonObject(json)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 对象转JsonElement
     *
     * @param obj 要转换的对象
     * @return JsonElement对象，转换失败返回null
     */
    fun objectToJsonElement(obj: Any?): JsonElement? {
        return try {
            if (obj == null) return null
            gsonInstance.toJsonTree(obj)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * JsonElement转对象
     *
     * @param element JsonElement对象
     * @param clazz 目标类型
     * @return 转换后的对象，转换失败返回null
     */
    fun <T> fromJsonElement(
        element: JsonElement?,
        clazz: Class<T>,
    ): T? {
        return try {
            if (element == null) return null
            gsonInstance.fromJson(element, clazz)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 深拷贝对象
     *
     * @param obj 要拷贝的对象
     * @param clazz 对象类型
     * @return 拷贝后的新对象，拷贝失败返回null
     */
    fun <T> deepCopy(
        obj: T?,
        clazz: Class<T>,
    ): T? {
        return try {
            if (obj == null) return null
            val json = toJson(obj) ?: return null
            fromJson(json, clazz)
        } catch (e: Exception) {
            e.printStackTrace()
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
        return try {
            if (json.isNullOrEmpty()) return false
            JsonParser.parseString(json)
            true
        } catch (e: Exception) {
            false
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
            val element = JsonParser.parseString(json)
            element.isJsonObject
        } catch (e: Exception) {
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
            val element = JsonParser.parseString(json)
            element.isJsonArray
        } catch (e: Exception) {
            false
        }
    }

    /**
     * 合并两个JsonObject
     *
     * @param target 目标JsonObject（会被修改）
     * @param source 源JsonObject
     * @return 合并后的JsonObject
     */
    fun mergeJsonObjects(
        target: JsonObject,
        source: JsonObject,
    ): JsonObject {
        for ((key, value) in source.entrySet()) {
            if (target.has(key) && target.get(key).isJsonObject && value.isJsonObject) {
                // 递归合并嵌套的JsonObject
                mergeJsonObjects(target.getAsJsonObject(key), value.asJsonObject)
            } else {
                target.add(key, value)
            }
        }
        return target
    }

    /**
     * 从JsonObject中安全获取字符串
     *
     * @param jsonObject JsonObject对象
     * @param key 键名
     * @param defaultValue 默认值
     * @return 获取到的字符串值，如果不存在或类型不匹配则返回默认值
     */
    fun getString(
        jsonObject: JsonObject?,
        key: String,
        defaultValue: String = "",
    ): String {
        return try {
            if (jsonObject == null || !jsonObject.has(key)) return defaultValue
            val element = jsonObject.get(key)
            if (element.isJsonNull) defaultValue else element.asString
        } catch (e: Exception) {
            defaultValue
        }
    }

    /**
     * 从JsonObject中安全获取整数
     *
     * @param jsonObject JsonObject对象
     * @param key 键名
     * @param defaultValue 默认值
     * @return 获取到的整数值，如果不存在或类型不匹配则返回默认值
     */
    fun getInt(
        jsonObject: JsonObject?,
        key: String,
        defaultValue: Int = 0,
    ): Int {
        return try {
            if (jsonObject == null || !jsonObject.has(key)) return defaultValue
            val element = jsonObject.get(key)
            if (element.isJsonNull) defaultValue else element.asInt
        } catch (e: Exception) {
            defaultValue
        }
    }

    /**
     * 从JsonObject中安全获取长整数
     *
     * @param jsonObject JsonObject对象
     * @param key 键名
     * @param defaultValue 默认值
     * @return 获取到的长整数值，如果不存在或类型不匹配则返回默认值
     */
    fun getLong(
        jsonObject: JsonObject?,
        key: String,
        defaultValue: Long = 0L,
    ): Long {
        return try {
            if (jsonObject == null || !jsonObject.has(key)) return defaultValue
            val element = jsonObject.get(key)
            if (element.isJsonNull) defaultValue else element.asLong
        } catch (e: Exception) {
            defaultValue
        }
    }

    /**
     * 从JsonObject中安全获取浮点数
     *
     * @param jsonObject JsonObject对象
     * @param key 键名
     * @param defaultValue 默认值
     * @return 获取到的浮点数值，如果不存在或类型不匹配则返回默认值
     */
    fun getFloat(
        jsonObject: JsonObject?,
        key: String,
        defaultValue: Float = 0f,
    ): Float {
        return try {
            if (jsonObject == null || !jsonObject.has(key)) return defaultValue
            val element = jsonObject.get(key)
            if (element.isJsonNull) defaultValue else element.asFloat
        } catch (e: Exception) {
            defaultValue
        }
    }

    /**
     * 从JsonObject中安全获取双精度浮点数
     *
     * @param jsonObject JsonObject对象
     * @param key 键名
     * @param defaultValue 默认值
     * @return 获取到的双精度浮点数值，如果不存在或类型不匹配则返回默认值
     */
    fun getDouble(
        jsonObject: JsonObject?,
        key: String,
        defaultValue: Double = 0.0,
    ): Double {
        return try {
            if (jsonObject == null || !jsonObject.has(key)) return defaultValue
            val element = jsonObject.get(key)
            if (element.isJsonNull) defaultValue else element.asDouble
        } catch (e: Exception) {
            defaultValue
        }
    }

    /**
     * 从JsonObject中安全获取布尔值
     *
     * @param jsonObject JsonObject对象
     * @param key 键名
     * @param defaultValue 默认值
     * @return 获取到的布尔值，如果不存在或类型不匹配则返回默认值
     */
    fun getBoolean(
        jsonObject: JsonObject?,
        key: String,
        defaultValue: Boolean = false,
    ): Boolean {
        return try {
            if (jsonObject == null || !jsonObject.has(key)) return defaultValue
            val element = jsonObject.get(key)
            if (element.isJsonNull) defaultValue else element.asBoolean
        } catch (e: Exception) {
            defaultValue
        }
    }

    /**
     * 从JsonObject中安全获取JsonObject
     *
     * @param jsonObject JsonObject对象
     * @param key 键名
     * @return 获取到的JsonObject，如果不存在或类型不匹配则返回null
     */
    fun getJsonObject(
        jsonObject: JsonObject?,
        key: String,
    ): JsonObject? {
        return try {
            if (jsonObject == null || !jsonObject.has(key)) return null
            val element = jsonObject.get(key)
            if (element.isJsonNull || !element.isJsonObject) null else element.asJsonObject
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 从JsonObject中安全获取JsonArray
     *
     * @param jsonObject JsonObject对象
     * @param key 键名
     * @return 获取到的JsonArray，如果不存在或类型不匹配则返回null
     */
    fun getJsonArray(
        jsonObject: JsonObject?,
        key: String,
    ): JsonArray? {
        return try {
            if (jsonObject == null || !jsonObject.has(key)) return null
            val element = jsonObject.get(key)
            if (element.isJsonNull || !element.isJsonArray) null else element.asJsonArray
        } catch (e: Exception) {
            null
        }
    }
}
