# ARouter 使用指南

## 简介
ARouter 是阿里巴巴开源的 Android 平台中对页面、服务提供路由功能的中间件，提倡的是简单且够用的理念。

## 已完成的配置

### 1. 添加依赖 (app/build.gradle.kts)

```kotlin
plugins {
    // ... 其他插件
    id("kotlin-kapt")
}

android {
    defaultConfig {
        // ... 其他配置

        // ARouter配置
        kapt {
            arguments {
                arg("AROUTER_MODULE_NAME", project.name)
            }
        }
    }
}

dependencies {
    // ARouter
    implementation("com.alibaba:arouter-api:1.5.2")
    kapt("com.alibaba:arouter-compiler:1.5.2")
}
```

### 2. 初始化 ARouter (MyApplication.kt)

```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // 初始化ARouter
        if (BuildConfig.DEBUG) {
            ARouter.openLog()     // 开启日志
            ARouter.openDebug()   // 开启调试模式
        }
        ARouter.init(this)
    }
}
```

### 3. 在 AndroidManifest.xml 中注册 Application

```xml
<application
    android:name=".MyApplication"
    ...>
</application>
```

## 使用示例

### 1. 声明路由

在 Activity 上使用 `@Route` 注解声明路由路径：

```kotlin
@Route(path = "/app/main")
class MainActivity : ComponentActivity() {
    // ...
}

@Route(path = "/app/second")
class SecondActivity : ComponentActivity() {
    // ...
}
```

### 2. 简单跳转

```kotlin
// 最简单的跳转
ARouter.getInstance()
    .build("/app/third")
    .navigation()
```

### 3. 带参数跳转

```kotlin
// 跳转并传递参数
ARouter.getInstance()
    .build("/app/second")
    .withString("name", "张三")
    .withInt("age", 25)
    .navigation()
```

### 4. 接收参数

在目标 Activity 中使用 `@Autowired` 注解接收参数：

```kotlin
@Route(path = "/app/second")
class SecondActivity : ComponentActivity() {

    @Autowired
    @JvmField
    var name: String? = null

    @Autowired
    @JvmField
    var age: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 必须调用 inject 方法来注入参数
        ARouter.getInstance().inject(this)

        // 现在可以使用 name 和 age 了
    }
}
```

### 5. 带动画跳转

```kotlin
ARouter.getInstance()
    .build("/app/second")
    .withString("name", "李四")
    .withInt("age", 30)
    .withTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    .navigation()
```

### 6. 其他常用功能

#### 获取 Fragment

```kotlin
val fragment = ARouter.getInstance()
    .build("/app/fragment")
    .navigation() as Fragment
```

#### 带回调的跳转

```kotlin
ARouter.getInstance()
    .build("/app/second")
    .navigation(this, object : NavigationCallback {
        override fun onFound(postcard: Postcard?) {
            // 找到路由
        }

        override fun onLost(postcard: Postcard?) {
            // 路由丢失
        }

        override fun onArrival(postcard: Postcard?) {
            // 跳转完成
        }

        override fun onInterrupt(postcard: Postcard?) {
            // 跳转被中断
        }
    })
```

#### 使用 startActivityForResult

```kotlin
ARouter.getInstance()
    .build("/app/second")
    .navigation(this, 100)  // 100 是 requestCode
```

#### 传递对象

```kotlin
// 传递 Serializable 对象
ARouter.getInstance()
    .build("/app/second")
    .withSerializable("user", user)
    .navigation()

// 传递 Parcelable 对象
ARouter.getInstance()
    .build("/app/second")
    .withParcelable("user", user)
    .navigation()
```

#### 传递 Bundle

```kotlin
val bundle = Bundle()
bundle.putString("key", "value")

ARouter.getInstance()
    .build("/app/second")
    .with(bundle)
    .navigation()
```

## 路由路径规范

建议的路径命名规范：
- 至少两级：`/模块名/页面名`
- 例如：
  - `/app/main` - 主页面
  - `/user/login` - 用户登录页
  - `/user/profile` - 用户资料页
  - `/order/detail` - 订单详情页

## 注意事项

1. **必须调用 inject()**：如果使用 `@Autowired` 注解接收参数，必须在 `onCreate()` 中调用 `ARouter.getInstance().inject(this)`

2. **路径必须唯一**：每个路由路径在整个应用中必须是唯一的

3. **调试模式**：在正式发布时，记得关闭 `ARouter.openDebug()`，否则有安全风险

4. **混淆配置**：如果开启了代码混淆，需要添加以下规则：

```proguard
-keep public class com.alibaba.android.arouter.routes.**{*;}
-keep public class com.alibaba.android.arouter.facade.**{*;}
-keep class * implements com.alibaba.android.arouter.facade.template.ISyringe{*;}

# 如果使用了 byType 的方式获取 Service，需添加下面规则，保护接口
-keep interface * implements com.alibaba.android.arouter.facade.template.IProvider

# 如果使用了 单类注入，即不定义接口实现 IProvider，需添加下面规则，保护实现
# -keep class * implements com.alibaba.android.arouter.facade.template.IProvider
```

## 更多功能

ARouter 还支持：
- 拦截器（Interceptor）
- 服务发现（Service）
- 依赖注入
- 降级策略
- 预处理服务

详细文档请参考：https://github.com/alibaba/ARouter

## 项目中的示例

运行项目后，在主页面可以看到三个按钮：
1. **示例1**：简单跳转到第三个页面
2. **示例2**：带参数跳转到第二个页面
3. **示例3**：带动画跳转到第二个页面

可以点击这些按钮查看实际效果。
