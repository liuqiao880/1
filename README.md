# TaskFlow

一款融合「高级报纸」美学风格的 AI 任务管理应用，支持 Web 端和 Android 端。AI 帮你把模糊的目标拆解为可执行的任务清单，告别拖延，从拆解开始。

---

## ✨ 功能亮点

- **🤖 AI 智能规划** — 输入目标，AI 自动拆解为多级任务清单（父子任务、优先级、时间节点）
- **✅ 任务管理** — 状态切换（待办/进行/完成）、优先级排序、番茄钟、搜索过滤
- **🎨 报纸风格美学** — 借鉴《纽约时报》《金融时报》设计语言，报纸红主色、墨色文字层次、纸张质感
- **🎨 4 种主题色** — 报纸红 / 墨黑 / 暗金 / 钢蓝，一键切换
- **🌓 深色模式** — 支持浅色/深色跟随系统
- **📱 双端同步** — Web + Android，React + Jetpack Compose
- **🔔 每日提醒** — 早 8 点推送今日待办（Web 端 Notification API）
- **🎙️ 语音输入** — 支持中文语音转文字（Web Speech API / Android SpeechRecognizer）
- **🔐 隐私优先** — API Key 不在本地持久化存储，随开随用

---

## 🚀 快速开始

### Web 端

```bash
# 安装依赖
npm install

# 启动开发服务器
npm run dev

# 构建生产版本
npm run build
```

访问 http://localhost:5173

### Android 端

```bash
cd android

# 构建 Debug APK
gradle assembleDebug

# 构建 Release APK（已配置签名）
gradle assembleRelease
```

APK 输出路径：`android/app/build/outputs/apk/release/app-release.apk`

---

## 📦 下载安装

最新 Release APK 可在 GitHub Releases 下载：

👉 [下载最新版 APK](https://github.com/liuqiao880/1/releases/latest)

---

## 🎯 使用指南

### AI 智能规划

1. 点击右下角悬浮按钮 → 选择「AI 智能规划」
2. 输入你的目标（如「学习 Python」「做一个项目」）
3. 可选：补充详细说明
4. 点击「开始规划」→ AI 生成任务清单
5. 勾选要保留的任务 → 「确认保存」

> 未配置 API Key 时自动进入演示模式，可体验完整流程。

### 配置 AI API

1. 点击右上角 ⚙️ 设置 → 「API 配置」
2. 填写 Provider、Base URL、API Key、Model
3. 点击「保存」

支持所有 OpenAI 兼容接口（OpenAI / 通义千问 / 智谱 / DeepSeek 等）。

### 任务操作

- **点击任务** — 切换完成状态
- **长按任务** — 进入多选模式，可批量删除
- **点击 ▶/▼** — 展开/收起子任务
- **番茄钟** — 点击任务旁的番茄图标开始专注计时

---

## 🛠️ 技术栈

### Web 端

| 模块 | 技术 |
|------|------|
| 框架 | React 18 + TypeScript |
| 构建 | Vite |
| 样式 | Tailwind CSS + CSS 变量主题系统 |
| 状态管理 | Zustand（带 localStorage 持久化） |
| AI 服务 | Fetch API + AbortController（30s 超时） |
| 语音输入 | Web Speech API（webkitSpeechRecognition） |
| 通知 | Notification API |

### Android 端

| 模块 | 技术 |
|------|------|
| 语言 | Kotlin |
| UI | Jetpack Compose + Material 3 |
| 架构 | MVVM + Clean Architecture（Domain/Data/UI） |
| 依赖注入 | Hilt |
| 导航 | Navigation Compose |
| 数据库 | Room |
| 偏好设置 | DataStore Preferences |
| 网络 | OkHttp + Gson |
| 语音输入 | SpeechRecognizer |

---

## 📂 项目结构

```
/workspace
├── src/                        # Web 端源码
│   ├── components/             # 组件（Modal/FAB/TaskList 等）
│   ├── pages/                  # 页面（Home/ChatDetail/ChatSettings）
│   ├── store/                  # Zustand 状态管理
│   ├── services/               # 业务服务（aiService）
│   ├── types/                  # TypeScript 类型定义
│   ├── App.tsx                 # 主应用入口
│   └── index.css               # 全局样式 + 主题变量
│
├── android/app/src/main/       # Android 端源码
│   ├── java/com/taskflow/app/
│   │   ├── domain/             # 领域层（模型/仓储接口/UseCase/服务）
│   │   ├── data/               # 数据层（Room 实体/DAO/仓储实现）
│   │   ├── di/                 # Hilt 依赖注入模块
│   │   ├── ui/                 # UI 层（Screen/Component/Theme/Navigation）
│   │   └── MainActivity.kt     # 主 Activity
│   └── res/                    # 资源文件
│
├── CHANGELOG.md                # 更新日志（Keep a Changelog 格式）
└── TaskFlow_Design_Doc.md      # 设计文档
```

---

## 🎨 设计系统

### 配色

| 名称 | 色值 | 用途 |
|------|------|------|
| NewspaperRed | `#C41E3A` | 主色（报纸红） |
| InkBlack | `#1A1A1A` | 主要文字 |
| InkGray | `#4A4A4A` | 次要文字 |
| InkLight | `#8A8A8A` | 辅助文字 |
| PaperWhite | `#FAFAFA` | 背景（浅色） |
| PaperLightGray | `#F5F5F5` | 表面变体 |
| LineSeparator | `#E0E0E0` | 分割线 |

### 字体

- **标题**：Serif 衬线字体（报纸标题感）
- **正文**：SansSerif 无衬线字体（可读性优先）

### 主题色切换

通过 CSS 变量 `--color-accent` / `--color-accent-dark` / `--color-accent-light` + `data-accent` 属性实现 4 色主题切换。

---

## 🔒 隐私说明

- API Key 仅保存在本地，不会上传到任何第三方服务器
- Web 端 API Key 不持久化到 localStorage，刷新后需重新输入
- Android 端 API Key 存储在 DataStore Preferences（应用沙箱内）
- 所有 AI 请求直接从客户端发送到你配置的 API 服务商

---

## 📝 更新日志

详见 [CHANGELOG.md](./CHANGELOG.md)

---

## 📄 开源协议

MIT License
