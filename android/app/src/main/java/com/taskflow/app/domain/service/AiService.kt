package com.taskflow.app.domain.service

import com.taskflow.app.domain.model.Task
import com.taskflow.app.domain.model.TaskPriority

class AiService {

    private val defaultSystemPrompt = """你是一位专业的任务拆分师，擅长将复杂的目标拆解为可执行的具体任务。

你的职责：
1. 深入理解用户的目标和需求
2. 将大目标拆解为层级清晰的任务清单（支持父子任务）
3. 为每个任务分配合适的优先级（1紧急/2普通/3低优）
4. 预估每个任务的大致时间节点
5. 给出合理的执行顺序建议

输出格式要求：请用自然语言简要分析，然后给出任务建议。"""

    suspend fun chat(
        messages: List<Pair<String, String>>,
        config: AiConfigData
    ): String {
        if (config.apiKey.isBlank()) {
            return mockResponse(messages)
        }
        return mockResponse(messages)
    }

    private fun mockResponse(messages: List<Pair<String, String>>): String {
        val lastUserMsg = messages.lastOrNull { it.first == "user" }?.second ?: ""

        val hasKeyword = fun(keywords: List<String>): Boolean {
            return keywords.any { lastUserMsg.contains(it) }
        }

        return when {
            hasKeyword(listOf("学习", "课程", "考试", "复习", "读书", "阅读")) ->
                "好的！我来帮你规划这个学习任务。根据你的目标，我拆分为以下几个阶段：\n\n" +
                "1. **制定学习计划** - 明确学习目标和时间安排\n" +
                "   - 梳理知识大纲\n" +
                "   - 分配每日学习时间\n\n" +
                "2. **基础概念学习** - 系统学习核心知识点\n" +
                "   - 阅读教材重点章节\n" +
                "   - 做章节配套练习\n" +
                "   - 整理笔记和重点\n\n" +
                "3. **实战练习** - 通过习题巩固知识\n" +
                "   - 完成模拟题\n" +
                "   - 错题整理与复习\n\n" +
                "4. **总复习与总结** - 查漏补缺，巩固成果\n\n" +
                "建议按顺序执行，每个阶段完成后再进入下一阶段。加油！💪"

            hasKeyword(listOf("项目", "开发", "产品", "上线", "需求", "设计")) ->
                "好的！这是一个项目型任务，我按照标准项目流程为你拆解：\n\n" +
                "1. **需求分析与规划** - 明确项目目标和范围\n" +
                "   - 收集用户需求\n" +
                "   - 确定功能优先级\n" +
                "   - 制定项目排期\n\n" +
                "2. **设计阶段** - 产品设计和技术方案\n" +
                "   - 原型设计\n" +
                "   - 技术方案评审\n" +
                "   - UI 设计稿\n\n" +
                "3. **开发实现** - 核心功能开发\n" +
                "   - 搭建项目架构\n" +
                "   - 开发核心功能模块\n" +
                "   - 联调与测试\n\n" +
                "4. **上线发布** - 部署与验证\n\n" +
                "建议用敏捷方式迭代推进，每个小阶段都有可交付成果。"

            hasKeyword(listOf("健身", "减肥", "运动", "锻炼", "跑步")) ->
                "好的！我来为你规划健身计划，循序渐进才能坚持下去：\n\n" +
                "1. **体能测试与目标设定**\n" +
                "   - 测量身体基础数据\n" +
                "   - 设定阶段性目标\n\n" +
                "2. **第一阶段：适应期（2周）** - 培养运动习惯\n" +
                "   - 每周3次轻度有氧运动\n" +
                "   - 学习正确的动作姿势\n" +
                "   - 调整饮食结构\n\n" +
                "3. **第二阶段：提升期（4周）** - 增加训练强度\n" +
                "   - 加入力量训练\n" +
                "   - 提高有氧强度\n\n" +
                "记住：坚持比强度更重要！每周至少休息1-2天给身体恢复。"

            else ->
                "好的！我来帮你拆解这个任务。根据你的描述，我建议这样安排：\n\n" +
                "1. **明确目标与范围**\n" +
                "   - 梳理具体需求\n" +
                "   - 确定优先级排序\n\n" +
                "2. **制定执行计划**\n" +
                "   - 拆分关键步骤\n" +
                "   - 预估每个步骤耗时\n\n" +
                "3. **执行并定期复盘**\n" +
                "   - 按计划推进\n" +
                "   - 每周回顾调整\n\n" +
                "有什么具体细节想补充的吗？可以继续告诉我，我来帮你细化。"
        }
    }

    fun parseTasksFromResponse(content: String): List<Task> {
        val todayTs = System.currentTimeMillis()
        val dayMs = 24 * 60 * 60 * 1000L

        val lines = content.lines()
        val tasks = mutableListOf<Task>()
        var currentParentIndex = -1

        for (line in lines) {
            val trimmed = line.trim()

            val parentMatch = Regex("""^(\d+)\.\s*\*\*(.+?)\*\*\s*-\s*(.+)$""").find(trimmed)
            if (parentMatch != null) {
                val title = parentMatch.groupValues[2]
                val description = parentMatch.groupValues[3]
                val task = Task(
                    id = 0,
                    title = title,
                    description = description,
                    priority = TaskPriority.MEDIUM,
                    dueDate = todayTs + tasks.size * dayMs,
                    aiGenerated = true,
                    children = emptyList()
                )
                tasks.add(task)
                currentParentIndex = tasks.size - 1
                continue
            }

            val childMatch = Regex("""^-\s*(.+)$""").find(trimmed)
            if (childMatch != null && currentParentIndex != -1) {
                val childTitle = childMatch.groupValues[1]
                val childTask = Task(
                    id = 0,
                    title = childTitle,
                    parentId = tasks[currentParentIndex].id,
                    priority = TaskPriority.MEDIUM,
                    aiGenerated = true
                )
                tasks[currentParentIndex] = tasks[currentParentIndex].copy(
                    children = tasks[currentParentIndex].children + childTask
                )
                continue
            }

            val simpleParentMatch = Regex("""^(\d+)\.\s*(.+)$""").find(trimmed)
            if (simpleParentMatch != null && trimmed.contains("**")) {
                continue
            }
        }

        return tasks
    }

    companion object {
        const val DEFAULT_BASE_URL = "https://api.openai.com/v1"
        const val DEFAULT_MODEL = "gpt-4o-mini"
    }
}

data class AiConfigData(
    val provider: String = "openai",
    val baseUrl: String = AiService.DEFAULT_BASE_URL,
    val apiKey: String = "",
    val model: String = AiService.DEFAULT_MODEL,
    val systemPrompt: String = ""
)
