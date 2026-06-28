# 彩票预测应用开发计划

## 1. 项目调研结论

### 1.1 现有项目结构

当前仓库 `MyDemoApp` 是一个多模块 Android 项目，采用以下分层架构：

```
MyDemoApp/
├── modules/
│   ├── lettin/          # 主应用入口 (lettin)
│   └── ticket/          # 彩票应用入口 (本计划主 App)
├── business/
│   ├── lettin-main/     # lettin 业务主模块 (Compose)
│   ├── lettin-device/   # 设备业务模块
│   ├── lettin-user/     # 用户业务模块
│   ├── http/            # HTTP 网络模块
│   └── ticket-main/     # 彩票业务主模块 (本计划核心模块)
├── common/
│   ├── service/         # 通用服务常量 (ARouter 路由表)
│   └── bean/            # 通用数据实体
└── core/
    ├── socket/          # TCP/UDP 封装
    ├── network/         # 网络扩展
    ├── database/        # Room 数据库封装
    ├── storage/         # 存储模块
    ├── log/             # XLog 日志
    ├── utils/           # 工具类
    └── mmkv/            # MMKV KV 存储
```

### 1.2 技术栈

| 组件 | 使用 | 位置 |
|------|------|------|
| 语言 | Kotlin 2.0.21 | [libs.versions.toml](file:///Users/linzhikuan/Desktop/dev_env/android_projects/MyDemoApp/gradle/libs.versions.toml#L3) |
| UI | Jetpack Compose (BOM 2024.09) | [libs.versions.toml](file:///Users/linzhikuan/Desktop/dev_env/android_projects/MyDemoApp/gradle/libs.versions.toml#L10) |
| 导航 | Navigation-Compose | [libs.versions.toml](file:///Users/linzhikuan/Desktop/dev_env/android_projects/MyDemoApp/gradle/libs.versions.toml#L24) |
| 依赖注入 | Hilt 2.48.1 | [libs.versions.toml](file:///Users/linzhikuan/Desktop/dev_env/android_projects/MyDemoApp/gradle/libs.versions.toml#L22) |
| 本地数据库 | Room 2.6.1 | [libs.versions.toml](file:///Users/linzhikuan/Desktop/dev_env/android_projects/MyDemoApp/gradle/libs.versions.toml#L19) |
| 网络 | Retrofit 2.9 + OkHttp 4.12 | [libs.versions.toml](file:///Users/linzhikuan/Desktop/dev_env/android_projects/MyDemoApp/gradle/libs.versions.toml#L17) |
| KV 存储 | MMKV 1.3.9 | [libs.versions.toml](file:///Users/linzhikuan/Desktop/dev_env/android_projects/MyDemoApp/gradle/libs.versions.toml#L15) |
| 路由 | ARouter 1.5.2 | [libs.versions.toml](file:///Users/linzhikuan/Desktop/dev_env/android_projects/MyDemoApp/gradle/libs.versions.toml#L16) |
| JSON | Gson 2.10.1 | [libs.versions.toml](file:///Users/linzhikuan/Desktop/dev_env/android_projects/MyDemoApp/gradle/libs.versions.toml#L13) |
| 日志 | Tencent XLog | [libs.versions.toml](file:///Users/linzhikuan/Desktop/dev_env/android_projects/MyDemoApp/gradle/libs.versions.toml#L14) |
| AGP | 8.10.1 | [libs.versions.toml](file:///Users/linzhikuan/Desktop/dev_env/android_projects/MyDemoApp/gradle/libs.versions.toml#L2) |

### 1.3 现有 `ticket` 模块现状

- `modules/ticket/` — 仅包含一个最小的 `MyApp.kt`（继承自 lettin），尚未有彩票相关业务。
- `business/ticket-main/` — 仅包含一个空壳 `MainActivity` + `HomeVM`，业务尚未实现。
- 引用参考：[MainActivity.kt](file:///Users/linzhikuan/Desktop/dev_env/android_projects/MyDemoApp/business/ticket-main/src/main/java/com/lzk/lettin/business/main/ui/MainActivity.kt)、[AppNav.kt](file:///Users/linzhikuan/Desktop/dev_env/android_projects/MyDemoApp/business/ticket-main/src/main/java/com/lzk/lettin/business/main/AppNav.kt)、[HomeVM.kt](file:///Users/linzhikuan/Desktop/dev_env/android_projects/MyDemoApp/business/ticket-main/src/main/java/com/lzk/lettin/business/main/vm/HomeVM.kt)

### 1.4 应用运行方式

- 运行 module `:modules:ticket` 时，`applicationId = "com.lzk.lettin"`。
- 当前依赖链：`:modules:ticket` → `:business:ticket-main` → `:business:lettin-device` → `:business:http` → core 模块。

---

## 2. 功能需求

基于用户的选择，实现**完整应用**：包括「历史数据采集 → 统计分析 → 简单算法预测 → 选号工具」四大模块，覆盖**双色球**与**大乐透**两种主流彩种。

```
┌──────────────────────────────────────────────────────────┐
│                      彩票预测 App                         │
│                                                          │
│  首页（彩种选择）                                         │
│  ├── 双色球 (6红 + 1蓝, 红 1-33, 蓝 1-16)                  │
│  └── 大乐透 (5前区 + 2后区, 前 1-35, 后 1-12)              │
│                                                          │
│  每个彩种包含以下 Tab：                                    │
│  ├── 历史开奖 — 列表展示近期开奖结果 + 下拉刷新              │
│  ├── 数据统计 — 频率图、冷热号、遗漏分析、和值、奇偶比       │
│  ├── 预测推荐 — 基于统计的简单算法推荐号码 (多套)            │
│  └── 选号工具 — 机选/自选/胆拖/复式，可保存到本地             │
│                                                          │
│  我的选号 — 查看已保存的号码/对比实际开奖                     │
└──────────────────────────────────────────────────────────┘
```

### 2.1 彩种规则抽象

- **双色球 (SSQ)**: `frontCount=6, frontRange=1..33, backCount=1, backRange=1..16`
- **大乐透 (DLT)**: `frontCount=5, frontRange=1..35, backCount=2, backRange=1..12`

所有算法与 UI 均基于以上抽象，便于未来扩展新彩种。

### 2.2 数据来源策略

- **主来源**：提供网络数据源接口，使用公共 API（如 `www.cwl.gov.cn` 第三方聚合）返回 JSON。
- **降级策略**：首次运行若无网络，内置**近 N 期的 mock 数据**（作为 asset JSON 文件），让应用立即可用。
- **数据持久化**：通过 Room 本地缓存，每次联网只拉取增量。

---

## 3. 模块划分与文件清单

沿用现有分层架构，在 `business/ticket-main` 内新增业务，避免破坏 `lettin` 模块的独立性。

### 3.1 新模块/目录一览

#### （1）`business/ticket-main/src/main/java/com/lzk/lettin/business/main/`

```
business/main/
├── data/                              # 数据层
│   ├── local/
│   │   ├── LotteryDatabase.kt         # Room DB (AppDatabase 风格统一)
│   │   ├── LotteryDrawDao.kt          # 开奖记录 DAO
│   │   └── SavedTicketDao.kt          # 保存号码 DAO
│   ├── remote/
│   │   ├── LotteryApi.kt              # Retrofit 接口定义
│   │   └── LotteryRemoteDataSource.kt # 网络数据源实现
│   ├── model/
│   │   ├── LotteryType.kt             # 彩种枚举 (SSQ/DLT)
│   │   ├── LotteryDraw.kt             # 开奖实体
│   │   └── SavedTicket.kt             # 选号实体
│   └── repository/
│       └── LotteryRepository.kt       # 数据仓库 (local + remote)
│
├── domain/                            # 领域层（业务算法）
│   ├── usecase/
│   │   ├── FetchHistoryUseCase.kt     # 拉取历史
│   │   └── PredictNumbersUseCase.kt   # 预测号码
│   └── analytics/
│       ├── FrequencyAnalyzer.kt       # 频率/冷热号
│       ├── MissAnalyzer.kt            # 遗漏分析
│       └── SumParityAnalyzer.kt       # 和值/奇偶
│
├── ui/
│   ├── screens/
│   │   ├── HomeScreen.kt              # 彩种选择页
│   │   ├── LotteryDetailScreen.kt     # 彩种主界面 (Tab 容器)
│   │   ├── HistoryTab.kt              # 历史开奖 Tab
│   │   ├── StatsTab.kt                # 数据统计 Tab (带图表)
│   │   ├── PredictTab.kt              # 预测推荐 Tab
│   │   ├── PickToolTab.kt             # 选号工具 Tab
│   │   └── MyTicketsScreen.kt         # 我的选号
│   └── vm/
│       ├── LotteryHomeVM.kt
│       ├── LotteryDetailVM.kt
│       ├── HistoryVM.kt
│       ├── StatsVM.kt
│       ├── PredictVM.kt
│       ├── PickToolVM.kt
│       └── MyTicketsVM.kt
│
└── AppNav.kt  ← 将扩展路由表（已有文件，需修改）
```

#### （2）`business/ticket-main/src/main/assets/`

```
assets/
├── ssq_mock.json                      # 双色球 mock 历史 (近 20 期)
└── dlt_mock.json                      # 大乐透 mock 历史 (近 20 期)
```

#### （3）`core/` 与 `common/` 的复用

- `core:database` — 直接复用 `AppDatabase.kt` 的风格，在 ticket-main 中建立独立 Room DB。
- `core:network` — 复用 `Ext.kt` 的扩展（若无则补齐 OkHttp/Retrofit 配置）。
- `core:log` — 通过 `logI/logD/logE` 输出调试信息。
- `core:mmkv` — 缓存用户偏好（默认彩种、最近预测参数）。
- `business:http` — 若无必要则不修改，由 ticket-main 自己实例化 Retrofit。

#### （4）需修改的既有文件

| 文件 | 修改内容 |
|------|---------|
| [modules/ticket/build.gradle.kts](file:///Users/linzhikuan/Desktop/dev_env/android_projects/MyDemoApp/modules/ticket/build.gradle.kts) | 新增 `:core:database`、`:core:network` 等依赖（如尚未包含） |
| [business/ticket-main/build.gradle.kts](file:///Users/linzhikuan/Desktop/dev_env/android_projects/MyDemoApp/business/ticket-main/build.gradle.kts) | 新增 Room/Retrofit/Compose 图表库依赖 |
| [business/ticket-main/.../AppNav.kt](file:///Users/linzhikuan/Desktop/dev_env/android_projects/MyDemoApp/business/ticket-main/src/main/java/com/lzk/lettin/business/main/AppNav.kt) | 扩展路由：首页 → 彩种详情 → 我的选号 |
| [business/ticket-main/.../MainActivity.kt](file:///Users/linzhikuan/Desktop/dev_env/android_projects/MyDemoApp/business/ticket-main/src/main/java/com/lzk/lettin/business/main/ui/MainActivity.kt) | 底部导航改造（首页/选号/设置） |
| [modules/ticket/.../MyApp.kt](file:///Users/linzhikuan/Desktop/dev_env/android_projects/MyDemoApp/modules/ticket/src/main/java/com/lzk/lettin/MyApp.kt) | 初始化 Room DB |
| [gradle/libs.versions.toml](file:///Users/linzhikuan/Desktop/dev_env/android_projects/MyDemoApp/gradle/libs.versions.toml) | 引入 `compose-charts`（或直接用 `Canvas` 自绘柱状图/折线图，避免引入额外依赖） |

---

## 4. 关键实现步骤

### 步骤 1：数据模型与 Room 持久化

**目标**：定义彩种枚举、开奖记录、选号记录三类数据，并让它们在本地可 CRUD。

- `LotteryType`：`enum class { SSQ, DLT }`，携带 `frontCount/frontRange/backCount/backRange`。
- `LotteryDraw`：`@Entity`，字段 `id / issueNo / date / frontNumbers(comma-sep) / backNumbers / type / poolAmount / firstCount / firstPrize / secondCount / secondPrize`。
- `SavedTicket`：`@Entity`，字段 `id / type / frontNumbers / backNumbers / createdAt / source(机选/自选/预测) / matchResult(JSON 或字段)`。
- `LotteryDatabase`：继承 `RoomDatabase`，暴露两个 DAO。通过 `modules/ticket/MyApp.kt` 在 onCreate 中初始化（参考现有 `RoomDataManager.init()`）。

### 步骤 2：Mock 数据 + 网络数据源

- **assets/ssq_mock.json**、**assets/dlt_mock.json**：包含近 20 期真实公开的历史数据（可由静态 JSON 给出，字段与 `LotteryDraw` 对齐）。
- `LotteryRemoteDataSource`：
  - 提供 `fun fetchLatest(type: LotteryType, count: Int): List<LotteryDraw>`。
  - 优先通过网络 API（如 `www.cwl.gov.cn` 的聚合接口）；失败时 fallback 到 assets JSON。
- `LotteryRepository`：协调 `local + remote`，策略为「本地先展示 → 后台刷新 → 去重合并」。
- `LotteryApi`：定义 Retrofit 接口（可先用占位 URL，接口暂时不调用以保证可编译，后续根据实际数据源调整）。

### 步骤 3：统计分析算法（domain/analytics）

算法全部基于**前 N 期历史数据**（默认 N=30，可在设置中调整）。

| 算法 | 说明 |
|------|------|
| **频率分析** | 统计每个号码在 N 期内出现次数，前/后区分开计算 |
| **冷热号** | 频率 Top-K 热号；频率 Bottom-K 冷号 |
| **遗漏分析** | 自上一次出现以来间隔的期数（最近 1 期内未出为 1） |
| **和值/奇偶/大小比** | 常规统计，用于分布约束 |
| **连号/重号** | 展示连续号/重号历史频率 |

实现细节：算法输入 `List<LotteryDraw>`，输出 `StatsResult(type, period, frontFreq, backFreq, frontMiss, backMiss, hotList, coldList, sumTrend, parityTrend)`。

### 步骤 4：预测号码算法（PredictNumbersUseCase）

> ⚠️ 重要提示：**彩票号码在数学上是独立均匀随机的，任何"预测"都不能真实提高中奖概率**。此处算法仅用于娱乐/兴趣演示，UI 中必须显式声明"仅供参考，不作为投注依据"。

提供三种可切换的预测策略：

1. **冷热号策略**：从热号池中取 70%，从冷号池补 30%（前后区分开），并做随机组合去重。
2. **遗漏回补策略**：高遗漏号作为"回补"候选，低遗漏号作为"热续"候选，加权随机。
3. **综合策略**：融合频率 + 遗漏 + 和值/奇偶约束（约束可配置）。

输出：一次给出 5 组号码，供用户参考。

### 步骤 5：选号工具（PickToolTab）

- **机选**：`kotlin.random.Random` 产生不重复组合，按规则校验（如和值范围约束可配置）。
- **自选**：展示一个号码选择网格（前区/后区分 Tab），选定后保存到 `SavedTicket`。
- **胆拖/复式**：
  - 胆码：必须包含的号码；拖码：从中组合填充。
  - 复式：简单多选全组合（若组合数过大，提示拆分）。
- **保存/查看**：写入 Room，并在「我的选号」页展示。

### 步骤 6：UI 与导航（Compose）

- **AppNav 路由表扩展**：
  - `home`（彩种选择）
  - `lottery/{type}`（彩种详情，内含 4 个 Tab）
  - `my-tickets`（我的选号）
- **底部导航**：首页 / 我的选号 / 设置（沿用现有 Scaffold + NavigationBar 风格）。
- **LotteryDetailScreen**：`TabRow` 切换 4 个 Tab；内部每个 Tab 通过各自 VM 驱动状态。
- **StatsTab 图表**：直接用 `Canvas` 自绘柱状图（号码 → 频率柱高）与折线图（和值趋势），**不引入第三方依赖**，以避免版本冲突。
- **号码球 UI**：自定义 Composable `NumberBall(num: Int, highlight: Boolean, size: Dp)`，双色球红色/蓝色区分，大乐透前区红/后区蓝。

### 步骤 7：ViewModel 状态架构

沿用项目中已有 MVI 风格（`BaseViewModel<State, Event, SideEffect>`）。对每个 VM：

- **State**：不可变 data class（例如 `data class HistoryState(val draws: List<LotteryDraw>, val loading: Boolean, val error: String?)`）。
- **Event**：`sealed interface`（例如 `Refresh / LoadMore`）。
- **SideEffect**：`sealed interface`（例如 `ShowToast / NavigateToDetail`）。

若 `BaseViewModel.kt` 已可用则直接继承；否则在 ticket-main 内补一个简化实现。

### 步骤 8：应用入口初始化

- 在 `modules/ticket/MyApp.kt` 中：
  - 调用 `LotteryDatabase.init(this)`（与现有 `RoomDataManager.init` 风格一致）。
  - 保留现有 `XLogConfig/ARouter/MMKVManager` 初始化。

### 步骤 9：依赖注入

- 与现有 `@HiltAndroidApp`、`@AndroidEntryPoint` 保持一致。
- 通过 `@Module @InstallIn(SingletonComponent::class)` 提供 `LotteryDatabase / LotteryRepository / LotteryApi / FetchHistoryUseCase / PredictNumbersUseCase` 等。

### 步骤 10：构建与测试

- 运行 `./gradlew :modules:ticket:assembleDebug` 验证构建。
- 在 Android 设备/模拟器安装并逐一验证 4 个 Tab 的核心功能。
- 为核心算法（频率/遗漏/随机组合不重复）编写 3~5 个简单单元测试（放在 `business/ticket-main/src/test/` 下）。

---

## 5. 依赖与外部考虑

### 5.1 新增依赖（仅必要，尽量复用）

| 用途 | 依赖来源 | 说明 |
|------|---------|------|
| Room 编译期处理 | `androidx.room:room-compiler` (KSP) | 已在 `libs.versions.toml` 定义 |
| Room KTX | `androidx.room:room-ktx` | 已定义 |
| Retrofit | `com.squareup.retrofit2:retrofit` | 已定义 |
| OkHttp 日志 | `com.squareup.okhttp3:logging-interceptor` | 已定义 |
| Compose 图表 | 自绘 `Canvas` | **不引入三方库**，避免版本与 Compose BOM 冲突 |
| Kotlin Random | 标准库 | 无需新增 |

### 5.2 外部 API 风险

- 公共彩票历史数据 API 可能不稳定或变更字段格式。
- **应对**：
  - 定义 `LotteryApi` 使用抽象接口，首次实现只返回空列表；真正接入时只需更换实现类，不影响 UI。
  - 永远保留 assets mock data fallback，保证首次启动可用。

### 5.3 合规声明（重要）

- 在「预测」和「选号工具」页面顶部，展示一条固定的 **免责声明**：
  > 「本应用仅用于学习与娱乐，彩票号码为独立随机事件，预测结果不作为投注依据。请理性购彩，未成年人禁止购彩。」

---

## 6. 风险与应对

| 风险 | 影响 | 应对策略 |
|------|------|---------|
| 网络 API 不可用 | 历史数据为空，应用显得"没内容" | 内置 mock 数据；首次启动先展示本地缓存 |
| Room 编译期 KSP 配置冲突 | 构建失败 | 统一使用项目已有的 KSP 版本；若 ticket-main 未启用 KSP，则在 build.gradle.kts 显式开启 |
| Compose 图表绘制性能 | 低端机上大量柱条重绘卡顿 | 限制展示号码数量 ≤ 35，使用 `Modifier.graphicsLayer` 与 `remember { mutableStateListOf }` 减少重组 |
| 预测算法被误解为"可提高中奖率" | 用户误解、误导 | UI 中多次明确免责声明；不使用"必中/稳赚"等措辞 |
| 随机组合去重在大号型（复式）指数爆炸 | UI 冻结 | 设置最大组合数上限（如 ≤200），超过则提示"请减少号码数" |
| 号码格式跨彩种不一致 | 数据解析 bug | `LotteryType` 枚举携带元数据，所有组合/解析算法统一走该元数据 |

---

## 7. 验收标准（Definition of Done）

- [ ] **构建**：`./gradlew :modules:ticket:assembleDebug` 成功。
- [ ] **首页**：可选择「双色球」「大乐透」，点击进入详情页。
- [ ] **历史开奖**：展示近期 N 期（N 默认 30，可配），含期号/日期/号码。
- [ ] **数据统计**：展示频率柱状图 + 冷热号列表 + 遗漏分析 + 和值趋势折线。
- [ ] **预测推荐**：至少 3 种策略可切换，一次输出 5 组号码。
- [ ] **选号工具**：机选/自选/胆拖/复式均可用，保存后出现在「我的选号」。
- [ ] **我的选号**：可查看已保存号码、支持删除。
- [ ] **离线降级**：首次断网启动也能看到 mock 历史数据。
- [ ] **无 lint/编译错误**。
- [ ] **免责声明**：在预测与选号页可见。
- [ ] **不引入新的三方图表库**（自绘 Canvas）。

---

## 8. 不做的事（Out of Scope）

- 不接入真实购彩/支付流程（仅为学习演示）。
- 不实现账号系统、云端同步。
- 不实现复杂的机器学习模型（如 LSTM/Transformer），仅做基于频率与遗漏的启发式算法。
- 不引入 Jetpack Compose 之外的 UI 框架（不混用 XML）。
- 不修改 `business/lettin-main` 与 `modules/lettin` 中的代码（保持 lettin 与 ticket 独立）。

---

## 9. 实施里程碑（顺序建议）

1. **数据层骨架**：LotteryType / LotteryDraw / SavedTicket + Room DB + Mock 数据。
2. **基础 UI 与导航**：首页 + 彩种详情 Tab 容器 + 空 Tab。
3. **历史开奖 Tab**：打通 Repository → UI 的第一条数据流。
4. **统计分析**：FrequencyAnalyzer / MissAnalyzer / StatsTab 图表。
5. **预测算法**：PredictNumbersUseCase + PredictTab。
6. **选号工具**：机选/自选/胆拖/复式 + SavedTicket 持久化。
7. **我的选号 + 底部导航调整**。
8. **网络数据源接入（可选）**：接入真实历史数据 API。
9. **联调与优化**：性能、UI polish、免责声明、设置页。
10. **测试 + 构建验证**。
