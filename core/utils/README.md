# Core Utils Module

这个模块提供了一系列实用工具类，用于简化Android开发中的常见任务。

## Gson工具类

### GsonUtils

`GsonUtils` 提供了基于Google Gson库的JSON序列化和反序列化功能。

#### 主要功能

##### 1. 对象转JSON
```kotlin
// 转换为JSON字符串
val json = GsonUtils.toJson(myObject)

// 转换为美化的JSON字符串（带缩进）
val prettyJson = GsonUtils.toPrettyJson(myObject)
```

##### 2. JSON转对象
```kotlin
// 基本类型转换
val user = GsonUtils.fromJson(jsonString, User::class.java)

// 使用Type转换
val type = object : TypeToken<List<User>>() {}.type
val users = GsonUtils.fromJson<List<User>>(jsonString, type)

// 使用TypeToken转换
val users = GsonUtils.fromJson(jsonString, object : TypeToken<List<User>>() {})
```

##### 3. JSON转List/Map
```kotlin
// JSON转List
val userList = GsonUtils.fromJsonToList(jsonString, User::class.java)

// JSON转ArrayList
val userArrayList = GsonUtils.fromJsonToArrayList(jsonString, User::class.java)

// JSON转Map
val map = GsonUtils.fromJsonToMap(jsonString)

// JSON转Map（指定value类型）
val userMap = GsonUtils.fromJsonToMap(jsonString, User::class.java)
```

##### 4. JsonObject/JsonArray操作
```kotlin
// 字符串转JsonObject
val jsonObject = GsonUtils.toJsonObject(jsonString)

// 字符串转JsonArray
val jsonArray = GsonUtils.toJsonArray(jsonString)

// 对象转JsonObject
val jsonObject = GsonUtils.objectToJsonObject(myObject)

// 对象转JsonElement
val jsonElement = GsonUtils.objectToJsonElement(myObject)

// JsonElement转对象
val user = GsonUtils.fromJsonElement(jsonElement, User::class.java)
```

##### 5. 安全获取JsonObject中的值
```kotlin
val jsonObject = GsonUtils.toJsonObject(jsonString)

// 获取字符串（带默认值）
val name = GsonUtils.getString(jsonObject, "name", "默认名称")

// 获取整数
val age = GsonUtils.getInt(jsonObject, "age", 0)

// 获取长整数
val id = GsonUtils.getLong(jsonObject, "id", 0L)

// 获取浮点数
val price = GsonUtils.getFloat(jsonObject, "price", 0f)

// 获取双精度浮点数
val amount = GsonUtils.getDouble(jsonObject, "amount", 0.0)

// 获取布尔值
val isActive = GsonUtils.getBoolean(jsonObject, "isActive", false)

// 获取嵌套的JsonObject
val address = GsonUtils.getJsonObject(jsonObject, "address")

// 获取JsonArray
val tags = GsonUtils.getJsonArray(jsonObject, "tags")
```

##### 6. 工具方法
```kotlin
// 验证JSON格式
val isValid = GsonUtils.isValidJson(jsonString)
val isValidObject = GsonUtils.isValidJsonObject(jsonString)
val isValidArray = GsonUtils.isValidJsonArray(jsonString)

// 深拷贝对象
val copy = GsonUtils.deepCopy(originalObject, User::class.java)

// 合并JsonObject
val merged = GsonUtils.mergeJsonObjects(target, source)

// 获取Gson实例
val gson = GsonUtils.getGson()
val prettyGson = GsonUtils.getPrettyGson()
```

### JsonUtils

`JsonUtils` 提供了基于org.json库的JSON操作功能，与Android原生API兼容。

#### 主要功能

##### 1. 创建JSON对象
```kotlin
// 创建空的JSONObject
val jsonObject = JsonUtils.createJsonObject()

// 创建空的JSONArray
val jsonArray = JsonUtils.createJsonArray()
```

##### 2. 解析JSON
```kotlin
// 字符串转JSONObject
val jsonObject = JsonUtils.parseObject(jsonString)

// 字符串转JSONArray
val jsonArray = JsonUtils.parseArray(jsonString)
```

##### 3. 类型转换
```kotlin
// Map转JSONObject
val jsonObject = JsonUtils.mapToJsonObject(map)

// List转JSONArray
val jsonArray = JsonUtils.listToJsonArray(list)

// JSONObject转Map
val map = JsonUtils.jsonObjectToMap(jsonObject)

// JSONArray转List
val list = JsonUtils.jsonArrayToList(jsonArray)
```

##### 4. 安全获取JSONObject中的值
```kotlin
val jsonObject = JsonUtils.parseObject(jsonString)

// 获取字符串
val name = JsonUtils.getString(jsonObject, "name", "默认值")

// 获取整数
val age = JsonUtils.getInt(jsonObject, "age", 0)

// 获取长整数
val id = JsonUtils.getLong(jsonObject, "id", 0L)

// 获取双精度浮点数
val amount = JsonUtils.getDouble(jsonObject, "amount", 0.0)

// 获取布尔值
val isActive = JsonUtils.getBoolean(jsonObject, "isActive", false)

// 获取嵌套的JSONObject
val address = JsonUtils.getJsonObject(jsonObject, "address")

// 获取JSONArray
val tags = JsonUtils.getJsonArray(jsonObject, "tags")
```

##### 5. JSONArray转List
```kotlin
// 获取字符串列表
val stringList = JsonUtils.getStringList(jsonArray)

// 获取整数列表
val intList = JsonUtils.getIntList(jsonArray)

// 获取长整数列表
val longList = JsonUtils.getLongList(jsonArray)

// 获取双精度浮点数列表
val doubleList = JsonUtils.getDoubleList(jsonArray)

// 获取布尔值列表
val booleanList = JsonUtils.getBooleanList(jsonArray)
```

##### 6. 工具方法
```kotlin
// 验证JSON格式
val isValid = JsonUtils.isValidJson(jsonString)
val isValidObject = JsonUtils.isValidJsonObject(jsonString)
val isValidArray = JsonUtils.isValidJsonArray(jsonString)

// 美化JSON字符串
val formatted = JsonUtils.formatJson(jsonString, indent = 4)

// 合并JSONObject
val merged = JsonUtils.mergeJsonObjects(target, source)

// 移除null值
val cleaned = JsonUtils.removeNullValues(jsonObject)
```

##### 7. Gson与org.json互转
```kotlin
// Gson的JsonObject转org.json的JSONObject
val jsonObject = JsonUtils.gsonToJsonObject(gsonJsonObject)

// Gson的JsonArray转org.json的JSONArray
val jsonArray = JsonUtils.gsonToJsonArray(gsonJsonArray)

// org.json的JSONObject转Gson的JsonObject
val gsonObject = JsonUtils.jsonObjectToGson(jsonObject)

// org.json的JSONArray转Gson的JsonArray
val gsonArray = JsonUtils.jsonArrayToGson(jsonArray)
```

## 使用示例

### 完整示例：用户数据处理

```kotlin
// 定义数据类
data class User(
    val id: Long,
    val name: String,
    val email: String,
    val age: Int,
    val isActive: Boolean
)

// 序列化
val user = User(1, "张三", "zhangsan@example.com", 25, true)
val json = GsonUtils.toJson(user)
println(json)
// 输出: {"id":1,"name":"张三","email":"zhangsan@example.com","age":25,"isActive":true}

// 反序列化
val userFromJson = GsonUtils.fromJson(json, User::class.java)
println(userFromJson?.name) // 输出: 张三

// 处理列表
val users = listOf(
    User(1, "张三", "zhangsan@example.com", 25, true),
    User(2, "李四", "lisi@example.com", 30, false)
)
val usersJson = GsonUtils.toJson(users)
val usersFromJson = GsonUtils.fromJsonToList(usersJson, User::class.java)

// 安全解析JSON
val jsonString = """{"id":1,"name":"张三","age":25}"""
val jsonObject = GsonUtils.toJsonObject(jsonString)
if (jsonObject != null) {
    val name = GsonUtils.getString(jsonObject, "name", "未知")
    val age = GsonUtils.getInt(jsonObject, "age", 0)
    val email = GsonUtils.getString(jsonObject, "email", "无邮箱") // 不存在的字段返回默认值
}

// 美化输出
val prettyJson = GsonUtils.toPrettyJson(user)
println(prettyJson)
/* 输出:
{
  "id": 1,
  "name": "张三",
  "email": "zhangsan@example.com",
  "age": 25,
  "isActive": true
}
*/
```

## 依赖配置

此模块已经配置了Gson依赖：

```kotlin
dependencies {
    implementation(libs.gson) // Gson 2.10.1
}
```

## 注意事项

1. 所有方法都包含异常处理，失败时返回null或默认值，不会抛出异常
2. `GsonUtils` 使用单例模式，内部维护了两个Gson实例（普通和美化输出）
3. `JsonUtils` 提供了与Android原生JSONObject/JSONArray的互操作性
4. 建议在网络请求、数据持久化等场景使用这些工具类
5. 对于复杂的泛型类型，建议使用TypeToken方式进行转换

## 其他工具类

### ByteUtils (Utils对象)

提供了字节数组相关的工具方法，包括：
- 字节数组转16进制字符串
- CRC16校验
- 字节序转换
- UDP JSON解析

详细使用方法请参考源码注释。
