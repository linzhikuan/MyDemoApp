package com.lzk.core.mmkv

import android.app.Application
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * MMKV 使用示例
 * 这个文件展示了如何在实际项目中使用 MMKV
 */

// ==================== 1. Application 初始化 ====================

@Suppress("ktlint:standard:no-consecutive-comments")
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 初始化 MMKV（必须在使用前调用）
        MMKVManager.initialize(this)
    }
}

// ==================== 2. 用户信息管理示例 ====================

/**
 * 用户信息管理类
 * 使用 MMKV 存储用户登录信息
 */
object UserPreferences {
    private val mmkv = MMKVManager.mmkvWithID("user_prefs")

    var userId: String?
        get() = mmkv.getString("user_id")
        set(value) {
            mmkv.putString("user_id", value)
        }

    var userName: String?
        get() = mmkv.getString("user_name")
        set(value) {
            mmkv.putString("user_name", value)
        }

    var userEmail: String?
        get() = mmkv.getString("user_email")
        set(value) {
            mmkv.putString("user_email", value)
        }

    var isLogin: Boolean
        get() = mmkv.getBoolean("is_login", false)
        set(value) {
            mmkv.putBoolean("is_login", value)
        }

    var token: String?
        get() = mmkv.getString("token")
        set(value) {
            mmkv.putString("token", value)
        }

    var loginTime: Long
        get() = mmkv.getLong("login_time", 0L)
        set(value) {
            mmkv.putLong("login_time", value)
        }

    /**
     * 保存用户信息
     */
    fun saveUserInfo(
        userId: String,
        userName: String,
        email: String,
        token: String,
    ) {
        this.userId = userId
        this.userName = userName
        this.userEmail = email
        this.token = token
        this.isLogin = true
        this.loginTime = System.currentTimeMillis()
    }

    /**
     * 清除用户信息（退出登录）
     */
    fun clear() {
        mmkv.deleteAll()
    }

    /**
     * 检查登录状态
     */
    fun isUserLoggedIn(): Boolean = isLogin && !token.isNullOrEmpty()
}

// ==================== 3. 应用配置管理示例 ====================

/**
 * 应用配置管理类
 * 使用 MMKV 存储应用设置
 */
object AppConfig {
    private val mmkv = MMKVManager.mmkvWithID("app_config")

    // 主题设置
    var theme: String
        get() = mmkv.getString("theme", "light") ?: "light"
        set(value) {
            mmkv.putString("theme", value)
        }

    // 语言设置
    var language: String
        get() = mmkv.getString("language", "zh") ?: "zh"
        set(value) {
            mmkv.putString("language", value)
        }

    // 通知开关
    var notificationEnabled: Boolean
        get() = mmkv.getBoolean("notification_enabled", true)
        set(value) {
            mmkv.putBoolean("notification_enabled", value)
        }

    // 字体大小
    var fontSize: Int
        get() = mmkv.getInt("font_size", 14)
        set(value) {
            mmkv.putInt("font_size", value)
        }

    // 自动播放
    var autoPlay: Boolean
        get() = mmkv.getBoolean("auto_play", false)
        set(value) {
            mmkv.putBoolean("auto_play", value)
        }

    // 缓存大小限制（MB）
    var cacheLimit: Int
        get() = mmkv.getInt("cache_limit", 500)
        set(value) {
            mmkv.putInt("cache_limit", value)
        }
}

// ==================== 4. 对象存储示例 ====================

/**
 * 用户信息数据类（使用 Parcelable）
 */
@Parcelize
data class User(
    val id: String,
    val name: String,
    val email: String,
    val avatar: String,
    val age: Int,
) : Parcelable

/**
 * 用户对象存储管理
 */
object UserObjectStorage {
    private val mmkv = MMKVManager.defaultMMKV()

    /**
     * 保存当前用户对象
     */
    fun saveCurrentUser(user: User) {
        mmkv.putParcelable("current_user", user)
    }

    /**
     * 获取当前用户对象
     */
    fun getCurrentUser(): User? = mmkv.getParcelable<User>("current_user")

    /**
     * 删除当前用户对象
     */
    fun clearCurrentUser() {
        mmkv.delete("current_user")
    }
}

// ==================== 5. 缓存管理示例 ====================

/**
 * 带过期时间的缓存管理
 */
object CacheManager {
    val mmkv = MMKVManager.mmkvWithID("cache")

    /**
     * 保存缓存数据（带过期时间）
     * @param key 缓存键
     * @param value 缓存值
     * @param expireTime 过期时间（毫秒），0表示永不过期
     */
    inline fun <reified T : Parcelable> putCache(
        key: String,
        value: T,
        expireTime: Long = 0,
    ) {
        mmkv.putParcelable(key, value)
        if (expireTime > 0) {
            mmkv.putLong("${key}_expire", System.currentTimeMillis() + expireTime)
        }
    }

    /**
     * 获取缓存数据
     * @param key 缓存键
     * @return 缓存值，如果过期或不存在则返回 null
     */
    internal inline fun <reified T : Parcelable> getCache(key: String): T? {
        val expireTime = mmkv.getLong("${key}_expire", 0)
        if (expireTime > 0 && System.currentTimeMillis() > expireTime) {
            // 缓存已过期，删除数据
            mmkv.delete(key)
            mmkv.delete("${key}_expire")
            return null
        }
        return mmkv.getParcelable<T>(key)
    }

    /**
     * 清理所有过期缓存
     */
    fun clearExpiredCache() {
        val keys = mmkv.allKeys() ?: return
        val currentTime = System.currentTimeMillis()

        keys.filter { it.endsWith("_expire") }.forEach { expireKey ->
            val expireTime = mmkv.getLong(expireKey, 0)
            if (currentTime > expireTime) {
                val dataKey = expireKey.removeSuffix("_expire")
                mmkv.delete(dataKey)
                mmkv.delete(expireKey)
            }
        }
    }

    /**
     * 清空所有缓存
     */
    fun clearAll() {
        mmkv.deleteAll()
    }
}

// ==================== 6. 敏感数据加密存储示例 ====================

/**
 * 敏感数据存储（加密）
 */
object SecureStorage {
    // 使用加密的 MMKV 实例
    private val mmkv = MMKVManager.mmkvWithIDAndCryptKey("secure_data", "your_secret_key_123")

    var password: String?
        get() = mmkv.getString("password")
        set(value) {
            mmkv.putString("password", value)
        }

    var apiKey: String?
        get() = mmkv.getString("api_key")
        set(value) {
            mmkv.putString("api_key", value)
        }

    var privateToken: String?
        get() = mmkv.getString("private_token")
        set(value) {
            mmkv.putString("private_token", value)
        }

    fun clear() {
        mmkv.deleteAll()
    }
}

// ==================== 7. 实际使用示例 ====================

/**
 * 在 Activity 或 Fragment 中使用示例
 */
class UsageExample {
    fun exampleUsage() {
        // 示例1：保存用户登录信息
        UserPreferences.saveUserInfo(
            userId = "12345",
            userName = "张三",
            email = "zhangsan@example.com",
            token = "xxx_token_xxx",
        )

        // 示例2：检查登录状态
        if (UserPreferences.isUserLoggedIn()) {
            val userName = UserPreferences.userName
            println("欢迎回来，$userName")
        }

        // 示例3：修改应用配置
        AppConfig.theme = "dark"
        AppConfig.fontSize = 16
        AppConfig.notificationEnabled = false

        // 示例4：保存用户对象
        val user =
            User(
                id = "12345",
                name = "张三",
                email = "zhangsan@example.com",
                avatar = "https://example.com/avatar.jpg",
                age = 25,
            )
        UserObjectStorage.saveCurrentUser(user)

        // 示例5：获取用户对象
        val currentUser = UserObjectStorage.getCurrentUser()
        currentUser?.let {
            println("用户名: ${it.name}, 邮箱: ${it.email}")
        }

        // 示例6：使用缓存（1小时后过期）
        val oneHour = 60 * 60 * 1000L
        CacheManager.putCache("user_data", user, oneHour)

        // 获取缓存
        val cachedUser = CacheManager.getCache<User>("user_data")

        // 示例7：存储敏感数据
        SecureStorage.password = "my_secure_password"
        SecureStorage.apiKey = "sk_test_123456"

        // 示例8：使用默认 MMKV 实例
        val mmkv = MMKVManager.defaultMMKV()
        mmkv.putString("key1", "value1")
        mmkv.putInt("key2", 100)
        mmkv.putBoolean("key3", true)

        val value1 = mmkv.getString("key1")
        val value2 = mmkv.getInt("key2")
        val value3 = mmkv.getBoolean("key3")

        // 示例9：批量操作
        val keys = mmkv.allKeys()
        println("总共有 ${mmkv.count()} 个键值对")
        println("占用空间: ${mmkv.totalSize() / 1024} KB")

        // 示例10：退出登录
        UserPreferences.clear()
        UserObjectStorage.clearCurrentUser()
        SecureStorage.clear()
    }
}
