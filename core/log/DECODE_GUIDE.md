# XLog 解码指南

## 问题：解码后的文件无法打开

如果您遇到解码后的 `.log` 文件无法打开或显示乱码的情况，请按照以下步骤排查：

## 🔍 步骤 1：使用详细模式检查文件

首先，使用 `-v` 参数运行解码脚本，查看详细信息：

```bash
python3 decode_xlog.py -f MyDemoApp_20251213.xlog -v
```

这会显示：
- 文件大小
- 文件类型（是否压缩）
- 文件头部的十六进制数据
- 是否检测到加密

## 🔐 步骤 2：检查是否使用了加密

### 2.1 查找加密配置

在您的项目中搜索 XLog 的初始化代码，查找是否配置了加密：

```bash
# 在项目根目录执行
grep -r "pubKey\|publicKey\|XLogConfig" --include="*.kt" --include="*.java"
```

### 2.2 常见的加密配置位置

检查以下文件：
- `Application` 类中的 `onCreate()` 方法
- 自定义的 `XLogConfig` 或 `LogConfig` 类
- 任何初始化日志系统的地方

### 2.3 加密配置示例

如果您的代码中有类似这样的配置：

```kotlin
XLogConfig.Builder()
    .pubKey("your_public_key_here")  // ← 这里配置了加密
    .build()
```

那么您需要使用相应的密钥来解码。

## 🔑 步骤 3：使用密钥解码

如果确认使用了加密，需要提供密钥：

```bash
# 使用密钥解码
python3 decode_xlog.py -f MyDemoApp_20251213.xlog -k "your_secret_key"
```

**注意：** 密钥应该与您在代码中配置的密钥一致。

## 📦 步骤 4：安装解密依赖

如果提示需要安装加密库：

```bash
pip3 install pycryptodome
```

## 🛠️ 步骤 5：检查 XLog 配置

### 5.1 查看当前项目的 XLog 配置

在项目中查找 XLog 的初始化代码：

```bash
# 查找 XLog 相关文件
find . -name "*Log*.kt" -o -name "*Log*.java" | grep -v build
```

### 5.2 常见配置选项

XLog 可能的配置：

| 配置项 | 说明 | 影响 |
|--------|------|------|
| `enableCompress` | 是否启用压缩 | 需要解压缩 |
| `pubKey` | 公钥加密 | 需要私钥解密 |
| `encryptKey` | 对称加密密钥 | 需要相同密钥解密 |

## 🔧 步骤 6：尝试不同的解码方式

### 方式 1：直接解码（无加密）

```bash
python3 decode_xlog.py -f MyDemoApp_20251213.xlog
```

### 方式 2：使用密钥解码

```bash
python3 decode_xlog.py -f MyDemoApp_20251213.xlog -k "your_key"
```

### 方式 3：批量解码

```bash
# 解码当前目录所有文件
python3 decode_xlog.py

# 解码指定目录
python3 decode_xlog.py -d /path/to/xlog/files
```

## 📝 步骤 7：查看解码后的文件

### 7.1 使用文本编辑器打开

```bash
# macOS
open MyDemoApp_20251213.log

# 或使用 cat 查看
cat MyDemoApp_20251213.log

# 查看前几行
head -n 20 MyDemoApp_20251213.log
```

### 7.2 检查文件内容

如果文件正确解码，应该能看到类似这样的日志：

```
2024-12-13 10:30:45.123 [INFO] MainActivity: onCreate called
2024-12-13 10:30:45.456 [DEBUG] NetworkManager: Connecting to server
```

如果看到的是乱码或二进制数据，说明：
1. 文件仍然是加密的
2. 使用的密钥不正确
3. 加密算法不匹配

## 🚨 常见问题解决

### 问题 1：提示"数据可能仍然是加密的"

**原因：** 日志文件使用了加密，但没有提供密钥或密钥不正确。

**解决方案：**
1. 在代码中找到加密密钥
2. 使用 `-k` 参数提供密钥
3. 如果是开发环境，考虑临时关闭加密

### 问题 2：文件解码后是空的

**原因：**
- 日志文件本身就是空的
- 应用还没有写入日志
- 日志被清理了

**解决方案：**
1. 检查应用是否正确初始化了 XLog
2. 确认有日志输出
3. 调用 `Logger.flush()` 确保日志写入

### 问题 3：解码后文件很大但无法打开

**原因：** 可能是二进制数据或特殊编码

**解决方案：**
```bash
# 检查文件类型
file MyDemoApp_20251213.log

# 查看文件的十六进制内容
hexdump -C MyDemoApp_20251213.log | head -n 20

# 尝试不同的编码
iconv -f GBK -t UTF-8 MyDemoApp_20251213.log > output.log
```

### 问题 4：提示需要安装 pycryptodome

**解决方案：**
```bash
# 安装加密库
pip3 install pycryptodome

# 如果上面的命令失败，尝试
pip3 install pycrypto
```

## 💡 调试技巧

### 1. 查看原始文件信息

```bash
# 查看文件大小
ls -lh MyDemoApp_20251213.xlog

# 查看文件头部（十六进制）
xxd MyDemoApp_20251213.xlog | head -n 10
```

### 2. 对比加密前后的文件

如果可以，在测试环境中：
1. 关闭加密，生成一个日志文件
2. 开启加密，生成另一个日志文件
3. 对比两个文件的差异

### 3. 使用官方工具

如果自定义脚本无法解码，尝试使用 XLog 官方的解码工具：

```bash
# 克隆 mars 仓库
git clone https://github.com/Tencent/mars.git
cd mars/mars/log/crypt

# 使用官方工具解码
python decode_mars_log_file.py /path/to/your.xlog
```

## 📚 相关资源

- [XLog GitHub](https://github.com/Tencent/mars/tree/master/mars/log)
- [XLog 文档](https://github.com/Tencent/mars/wiki/Mars-Android-Log-SDK-Guide)
- 项目内文档：
  - `README.md` - 基本使用说明
  - `USAGE_EXAMPLE.md` - 使用示例
  - `XLOG_SETUP_OPTIONS.md` - 配置选项

## 🆘 仍然无法解决？

如果按照上述步骤仍然无法解码，请提供以下信息：

1. 运行 `python3 decode_xlog.py -f your.xlog -v` 的完整输出
2. XLog 的初始化代码
3. 文件的十六进制头部：`xxd your.xlog | head -n 5`
4. 是否在生产环境还是开发环境

## 🎯 快速解决方案

如果您只是想快速查看日志，最简单的方法是：

### 方案 1：在开发环境关闭加密

临时修改 XLog 配置，关闭加密：

```kotlin
XLogConfig.Builder()
    // .pubKey("xxx")  // 注释掉加密配置
    .enableCompress(true)  // 保留压缩
    .build()
```

### 方案 2：使用 Logcat

在 Debug 模式下，日志会同时输出到 Logcat：

```bash
# 使用 adb 查看实时日志
adb logcat | grep "YourTag"

# 保存日志到文件
adb logcat > logcat.txt
```

### 方案 3：导出未加密的日志

在应用中添加一个调试功能，导出未加密的日志：

```kotlin
fun exportPlainLog() {
    val logDir = XLogConfig.getLogDir(context)
    val outputFile = File(context.getExternalFilesDir(null), "plain_log.txt")

    // 读取并解码日志
    // ... 实现导出逻辑
}
```

---

**提示：** 建议在开发环境中关闭日志加密，方便调试。在生产环境中再启用加密保护用户隐私。
