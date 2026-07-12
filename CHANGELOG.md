# 更新日志

本项目所有显著变更均会记录在此文件中。

格式参考 [Keep a Changelog](https://keepachangelog.com/zh-CN/1.1.0/)。

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
