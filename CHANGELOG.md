# 更新日志

本项目所有显著变更均会记录在此文件中。

格式参考 [Keep a Changelog](https://keepachangelog.com/zh-CN/1.1.0/)。

---

## [v0.6.0] - 2026-07-12

### 🔧 全维度测试修复 `[Web✅ Android✅]`

基于 6 维度测试检查报告（功能/兼容性/异常容错/UI交互/安全/性能），修复 34 项问题：

#### 高危修复
- **SettingsModal 编译错误**：showColorPicker 从 onClick 中调 @Composable 改为状态驱动 AlertDialog
- **SettingsModal 颜色选择器无法关闭**：onDismissRequest 从空 lambda 改为正确关闭
- **SettingsModal 缺少 border import**：添加 `import androidx.compose.foundation.border`
- **SettingsModal Row gap API 不兼容**：移除 `gap` 参数改用 Spacer
- **AiService.chat() 永远返回 mock**：实现真实 OkHttp 请求，支持超时和异常处理
- **AiPlanModal 使用空 AiConfigData**：改为注入 PreferencesRepository 读取真实配置
- **AiPlanModal 无 try-catch**：添加 ERROR 状态和重试功能
- **ChatDetailScreen 添加按钮空实现**：改为弹出 DropdownMenu
- **ChatDetailScreen 麦克风空实现**：集成 Android SpeechRecognizer 语音输入

#### 中危修复
- **SettingsModal API Key 明文显示**：改为 PasswordVisualTransformation + 眼睛图标切换
- **SettingsModal remember key 不同步**：localApiKey 等添加 `remember(key)` 参数
- **ChatDetailScreen 紫色渐变**：全部替换为 MaterialTheme.colorScheme.primary 主题色
- **ChatDetailViewModel 无 error 状态**：添加 error 字段 + clearError 方法 + regenerateLastResponse
- **HomeViewModel 搜索无防抖**：添加 300ms debounce
- **Web 通知定时器 setInterval 泄漏**：分离 timer 和 interval，关闭时均清除
- **Web aiService fetch 无超时**：添加 AbortController 30 秒超时
- **Web API Key 明文存 localStorage**：partialize 中排除 apiKey 不持久化
- **AiService.parseTasksFromResponse 格式不一致**：优先 JSON 解析，回退正则，与 Web 端对齐

#### 低危修复
- **SettingsModal 无用 import**：删除 ViewModelScoped 和 @Inject
- **AiPlanModal 任务选择无视觉反馈**：添加 primaryContainer 背景色
- **SettingsModal 取消按钮不恢复值**：取消时重置为 DataStore 原值

## [v0.5.0] - 2026-07-12

### 🎨 报纸风格美术升级 `[Web✅ Android✅]`

借鉴《纽约时报》《金融时报》等高级报纸的设计语言，全面升级 UI 风格：

#### 配色
- 主色：NewspaperRed (#C41E3A) — 标志性报纸红
- 文字层次：InkBlack / InkGray / InkLight — 墨色三级灰阶
- 背景：PaperWhite (#FAFAFA) / PaperCream (#FDFBF7) — 纸张质感
- 分隔线：LineSeparator (#E0E0E0) — 细线分隔
- 优先级：紧急=红 / 普通=暗金(#B8860B) / 低优=钢蓝(#4682B4)

#### 排版
- 标题字体：Georgia / Serif 系列（font-serif）
- 正文字体：PingFang SC / Sans-serif 系列（font-sans）
- 标题用 Serif + SemiBold，正文用 Sans + Regular

#### 形状与装饰
- 圆角缩小为 4-8dp，更克制精致
- 复选框：描边圆形（border + transparent）代替实心填充
- 红色短线条前缀装饰（newspaper-accent-line）
- 细边框分隔代替大间距 Card
- FAB 按钮：方形代替圆形，hover 变红

#### 组件变更
- TopNavbar：红色方块 Logo + Serif 标题 + 红色短线装饰
- FilterTabs：墨黑填充选中态 + 底部细线
- TaskGroup：红色短线前缀 + Serif 大写标题 + 数字计数
- TaskItem：列表项+细线分隔（去掉 Card 阴影），0.5px 优先级色条
- ChatList/ChatDetail：红色方块图标 + Serif 标题 + 细线分隔列表
- PomodoroPanel：报纸红主色调 + Serif 字体计时

### 📱 Android 端
- Theme.kt / Typography.kt / Shapes.kt 全部重写为报纸风格
- TopAppBar / TaskList / PomodoroPanel 样式同步升级
- 数据库版本升至 v4（ChatMessageEntity 新增 ForeignKey/Index）

### 🌐 Web 端
- tailwind.config.js 新增 newspaper / ink / paper / line / priority 色板 + serif/sans 字体族
- index.css 新增报纸风格装饰线类 + Serif 字体规则
- TopNavbar / FilterTabs / TaskGroup / TaskItem / TaskList / FAB 全部重写
- ChatList / ChatDetail / PomodoroPanel 报纸风格适配
- Home 页面：桌面端外框改为报纸版面风格（细边框 + 方形刘海）

### 🔧 优化
- 修复 60+ 项代码质量问题（闪退、编译错误、Hilt 注入、协程泄漏等）
- 响应式架构：MutableStateFlow + flatMapLatest 替代命令式加载
- ChatMessageEntity Gson 序列化修复（suggestedTasks 不再为 null）

---

## [v0.4.0] - 2026-07-11

### ✨ 新增
- **AI 对话功能** `[Web✅ Android✅]`
  - 对话列表页：展示历史对话，支持新建/删除
  - 对话详情页：消息气泡（用户/AI 双侧）、输入框、发送
  - AI 模拟响应：学习/项目/健身等场景智能拆解
  - 空状态引导 + 建议问题快捷入口
  - 三点跳动加载动画
  - Web 端左滑删除对话；Android 端删除按钮

### 📱 Android 端
- 新增 Navigation 导航架构（Home / ChatList / ChatDetail）
- TopBar 新增 ✨ Sparkles 图标进入 AI 对话
- 数据库升级至 v3，新增 `chats`、`chat_messages` 两张表
- 新增 DI 绑定：ChatRepository、AiService、ChatDao

### 🌐 Web 端
- ChatList / ChatDetail 页面 + useChatStore 状态管理
- 左滑删除对话交互

### 🔧 优化
- RepositoryModule 注入 AiService 单例
- DateUtils 新增 `formatChatTime` 方法

---

## [v0.3.0] - 2026-07-11

### ✨ 新增
- **番茄钟功能** `[Web✅ Android✅]`
  - 三阶段计时：专注(25min) / 短休息(5min) / 长休息(15min)
  - 每 4 个番茄钟后自动进入长休息
  - 开始/暂停/重置/跳过控制
  - 任务卡片番茄按钮：点击启动对应任务的番茄钟
  - 底部弹出式计时器面板（圆形进度条 + 倒计时）
  - 完成一个专注阶段自动累计任务的番茄数

### 📱 Android 端
- Task 模型新增 `pomodoroCount` 字段 + `PomodoroPhase` 枚举 + `PomodoroState` 数据类
- TaskEntity / TaskMappers 同步 `pomodoroCount`
- TaskDao 新增 `incrementPomodoroCount` 方法
- 新增 `IncrementPomodoroUseCase`
- HomeViewModel 新增番茄钟状态管理（startPomodoro / togglePomodoro / resetPomodoro / skipPomodoro）
- 新增 PomodoroPanel 组件
- TaskList 任务卡片新增番茄按钮

### 🌐 Web 端
- usePomodoroStore 番茄钟状态管理
- PomodoroTimer 计时器组件
- TaskItem 新增番茄按钮 + 番茄数显示
- 任务模型扩展 `pomodoroCount` 字段

### 🗄️ 数据层
- Android 数据库升级至 v2（TaskEntity 新增 `pomodoroCount` 列）

---

## [v0.2.0] - 2026-07-10

### ✨ 新增
- **任务搜索功能** `[Web✅ Android✅]`
  - 顶部搜索栏，支持标题 + 描述模糊搜索
  - 搜索结果实时过滤
  - 空结果友好提示

- **多选批量操作** `[Web✅ Android✅]`
  - 长按任务进入多选模式
  - 批量删除 / 批量完成
  - 顶部多选操作栏

- **设置页面** `[Web✅ Android✅]`
  - 主题切换（亮色/暗色/跟随系统）
  - AI 配置（Provider / API Key / Model）
  - 数据管理

### 🎨 UI
- 暗色模式适配（CSS 变量 / Material3 动态配色）
- 空状态插画 + 引导文案
- 任务卡片完成动画（删除线 + 透明度）
- 左滑删除 + Snackbar 撤销（Web 端）

### 📱 Android 端
- Hilt 依赖注入搭建完成
- Room 数据库 v1 基础结构（TaskEntity / TaskDao）
- DataStore 偏好存储（主题模式、折叠状态等）
- HomeViewModel + TaskRepository 架构
- TaskList / TaskItem / TopAppBar / SettingsDialog 组件

### 🌐 Web 端
- Zustand 状态管理（useTaskStore / useThemeStore）
- React + TypeScript + Vite 项目搭建
- localStorage 持久化

---

## [v0.1.0] - 2026-07-09

### ✨ 新增
- **任务基础 CRUD** `[Web✅ Android✅]`
  - 新增任务（标题、描述、优先级、截止日期）
  - 编辑 / 删除任务
  - 复选框切换完成状态（带触感反馈）
  - 父子任务层级结构
  - 父任务进度条（子任务完成度）

- **动态时间线视图** `[Web✅ Android✅]`
  - 任务按"今天" / "明天" / "本周" / "更晚"自动分组
  - 顶部快速筛选标签

- **AI 智能规划** `[Web✅]`
  - 手动触发 AI 拆解子任务
  - 预览确认 + 编辑调整
  - 失败降级提示

### 📱 Android 端
- Jetpack Compose + Material3 项目初始化
- 基础页面框架搭建

### 🌐 Web 端
- React + TypeScript + Vite 项目初始化
- Tailwind CSS 样式框架接入

### 📄 文档
- TaskFlow 设计文档 V1.3.0 锁档
