# MMKV 使用指南

MMKV 是腾讯开源的高性能键值存储框架，基于 mmap 内存映射，性能远超 SharedPreferences。

## 特性

- ✅ 高性能：基于 mmap，读写速度快
- ✅ 稳定可靠：经过微信等亿级用户验证
- ✅ 多进程支持：支持跨进程数据共享
- ✅ 加密支持：支持数据加密存储
- ✅ 易于使用：提供丰富的扩展函数

## 快速开始

### 1. 初始化

在 Application 的 `onCreate()` 方法中初始化 MMKV：

```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 初始化 MMKV
        MMKVManager.initialize(this)
    }
}
```

### 2. 基础使用

#### 使用默认实例

```kotlin
// 获取默认实例
val mmkv = MMKVManager.defaultMMKV()

// 存储数据
mmkv.putString("username", "张三")
mmkv.putInt("age", 25)
mmkv.putBoolean("isLogin", true)
mmkv.putLong("timestamp", System.currentTimeMillis())
mmkv.putFloat("score", 98.5f)
mmkv.putDouble("balance", 1234.56)

// 读取数据
val username = mmkv.getString("username") // "张三"
val age = mmkv.getInt("age") // 25
val isLogin = mmkv.getBoolean("isLogin") // true
val timestamp = mmkv.getLong("timestamp")
val score = mmkv.getFloat("score")
val balance = mmkv.getDouble("balance")

// 使用默认值
val nickname = mmkv.getString("nickname", "游客") // 如果不存在返回"游客"
val level = mmkv.getInt("level", 1) // 如果不存在返回1
```

#### 使用指定ID的实例

```kotlin
// 创建用户配置专用的MMKV实例
val userConfig = MMKVManager.mmkvWithID("user_config")

userConfig.putString("theme", "dark")
userConfig.putBoolean("notification", true)

val theme = userConfig.getString("theme")
val notification = userConfig.getBoolean("notification")
```

#### 使用加密实例

```kotlin
// 创建加密的MMKV实例
val secureMMKV = MMKVManager.mmkvWithIDAndCryptKey("secure_data", "my_secret_key_123")

// 存储敏感数据
secureMMKV.putString("password", "encrypted_password")
secureMMKV.putString("token", "user_token_xxx")

// 读取敏感数据
val password = secureMMKV.getString("password")
val token = secureMMKV.getString("token")
```

### 3. 高级用法

#### 存储集合类型

```kotlin
val mmkv = MMKVManager.defaultMMKV()

// 存储 Set<String>
val tags = setOf("Android", "Kotlin", "MMKV")
mmkv.putStringSet("tags", tags)

// 读取 Set<String>
val savedTags = mmkv.getStringSet("tags")
```

#### 存储字节数组

```kotlin
val mmkv = MMKVManager.defaultMMKV()

// 存储字节数组
val bytes = "Hello MMKV".toByteArray()
mmkv.putBytes("data", bytes)

// 读取字节数组
val savedBytes = mmkv.getBytes("data")
val text = savedBytes?.let { String(it) }
```

#### 存储 Parcelable 对象

```kotlin
// 定义 Parcelable 对象
@Parcelize
data class User(
    val id: Int,
    val name: String,
    val email: String
) : Parcelable

val mmkv = MMKVManager.defaultMMKV()

// 存储对象
val user = User(1, "张三", "zhangsan@example.com")
mmkv.putParcelable("current_user", user)

// 读取对象
val savedUser = mmkv.getParcelable<User>("current_user")
```

### 4. 数据管理

#### 检查键是否存在

```kotlin
val mmkv = MMKVManager.defaultMMKV()

if (mmkv.contains("username")) {
    println("用户名已存在")
}
```

#### 删除数据

```kotlin
val mmkv = MMKVManager.defaultMMKV()

// 删除单个键
mmkv.delete("username")

// 删除多个键
mmkv.delete(arrayOf("username", "age", "email"))

// 清空所有数据
mmkv.deleteAll()
```

#### 获取所有键

```kotlin
val mmkv = MMKVManager.defaultMMKV()

val keys = mmkv.allKeys()
keys?.forEach { key ->
    println("Key: $key")
}
```

#### 获取统计信息

```kotlin
val mmkv = MMKVManager.defaultMMKV()

// 获取键值对数量
val count = mmkv.count()
println("总共有 $count 个键值对")

// 获取占用空间大小（字节）
val size = mmkv.totalSize()
println("占用空间: ${size / 1024} KB")
```

### 5. 数据同步

```kotlin
val mmkv = MMKVManager.defaultMMKV()

// 同步写入磁盘（阻塞）
mmkv.sync()

// 异步写入磁盘（非阻塞）
mmkv.async()
```

### 6. 多进程模式

```kotlin
// 创建多进程共享的MMKV实例
val multiProcessMMKV = MMKVManager.mmkvWithID("multi_process", MMKV.MULTI_PROCESS_MODE)

// 在不同进程中都可以访问相同的数据
multiProcessMMKV.putString("shared_data", "跨进程共享的数据")
```

## 实际应用场景

### 场景1：用户登录信息管理

```kotlin
object UserPreferences {
    private val mmkv = MMKVManager.mmkvWithID("user_prefs")

    var userId: String?
        get() = mmkv.getString("user_id")
        set(value) = mmkv.putString("user_id", value)

    var userName: String?
        get() = mmkv.getString("user_name")
        set(value) = mmkv.putString("user_name", value)

    var isLogin: Boolean
        get() = mmkv.getBoolean("is_login", false)
        set(value) = mmkv.putBoolean("is_login", value)

    var token: String?
        get() = mmkv.getString("token")
        set(value) = mmkv.putString("token", value)

    fun clear() {
        mmkv.deleteAll()
    }
}

// 使用
UserPreferences.userId = "12345"
UserPreferences.userName = "张三"
UserPreferences.isLogin = true
UserPreferences.token = "xxx_token_xxx"

// 读取
if (UserPreferences.isLogin) {
    val name = UserPreferences.userName
    println("欢迎回来，$name")
}

// 退出登录
UserPreferences.clear()
```

### 场景2：应用配置管理

```kotlin
object AppConfig {
    private val mmkv = MMKVManager.mmkvWithID("app_config")

    var theme: String
        get() = mmkv.getString("theme", "light") ?: "light"
        set(value) = mmkv.putString("theme", value)

    var language: String
        get() = mmkv.getString("language", "zh") ?: "zh"
        set(value) = mmkv.putString("language", value)

    var notificationEnabled: Boolean
        get() = mmkv.getBoolean("notification_enabled", true)
        set(value) = mmkv.putBoolean("notification_enabled", value)

    var fontSize: Int
        get() = mmkv.getInt("font_size", 14)
        set(value) = mmkv.putInt("font_size", value)
}

// 使用
AppConfig.theme = "dark"
AppConfig.notificationEnabled = false
AppConfig.fontSize = 16
```

### 场景3：缓存管理

```kotlin
object CacheManager {
    private val mmkv = MMKVManager.mmkvWithID("cache")

    fun <T : Parcelable> putCache(key: String, value: T, expireTime: Long = 0) {
        mmkv.putParcelable(key, value)
        if (expireTime > 0) {
            mmkv.putLong("${key}_expire", System.currentTimeMillis() + expireTime)
        }
    }

    inline fun <reified T : Parcelable> getCache(key: String): T? {
        val expireTime = mmkv.getLong("${key}_expire", 0)
        if (expireTime > 0 && System.currentTimeMillis() > expireTime) {
            // 缓存已过期
            mmkv.delete(key)
            mmkv.delete("${key}_expire")
            return null
        }
        return mmkv.getParcelable<T>(key)
    }

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
}
```

## 性能对比

| 操作 | MMKV | SharedPreferences |
|------|------|-------------------|
| 写入 | ~1ms | ~10ms |
| 读取 | ~0.1ms | ~1ms |
| 批量写入 | 快 | 慢 |
| 多进程 | 支持 | 不稳定 |

## 注意事项

1. **初始化时机**：必须在使用前调用 `MMKVManager.initialize(context)`，建议在 Application 中初始化
2. **加密密钥**：使用加密功能时，密钥需要妥善保管，丢失后数据无法恢复
3. **多进程**：如需多进程共享数据，使用 `MMKV.MULTI_PROCESS_MODE`
4. **数据迁移**：从 SharedPreferences 迁移时，可以使用 MMKV 的导入功能
5. **ProGuard**：MMKV 已经包含混淆规则，无需额外配置

## 从 SharedPreferences 迁移

```kotlin
// 导入 SharedPreferences 数据到 MMKV
val sp = getSharedPreferences("old_prefs", Context.MODE_PRIVATE)
val mmkv = MMKVManager.mmkvWithID("new_prefs")
mmkv.importFromSharedPreferences(sp)

// 清空原 SharedPreferences（可选）
sp.edit().clear().apply()
```

## 更多资源

- [MMKV GitHub](https://github.com/Tencent/MMKV)
- [官方文档](https://github.com/Tencent/MMKV/wiki)
