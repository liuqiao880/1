package com.taskflow.app.domain.service

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.taskflow.app.domain.model.Task
import com.taskflow.app.domain.model.TaskPriority
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

class AiService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

    private val gson = Gson()

    private val defaultSystemPrompt = """你是一位专业的任务拆分师，擅长将复杂的目标拆解为可执行的具体任务。

你的职责：
1. 深入理解用户的目标和需求
2. 将大目标拆解为层级清晰的任务清单（支持父子任务）
3. 为每个任务分配合适的优先级（1紧急/2普通/3低优）
4. 预估每个任务的大致时间节点
5. 给出合理的执行顺序建议

输出格式要求：
请以 JSON 格式输出任务列表，格式如下：
{
  "tasks": [
    {
      "title": "任务标题",
      "description": "任务描述（可选）",
      "priority": 1,
      "dueDaysFromNow": 0,
      "children": [...]
    }
  ]
}

重要：
- 先回应用户，用自然语言简要分析和说明
- 然后在回复末尾用 ```json 代码块包裹任务 JSON
- 优先级 1=紧急，2=普通，3=低优
- dueDaysFromNow 表示从今天起的天数偏移
- 支持嵌套子任务，最多 2 层"""

    suspend fun chat(
        messages: List<Pair<String, String>>,
        config: AiConfigData
    ): String = withContext(Dispatchers.IO) {
        if (config.apiKey.isBlank()) {
            return@withContext "演示模式：未配置 API Key，以下为模拟数据。\n\n${mockResponse(messages)}"
        }

        val systemMsg = mapOf("role" to "system", "content" to (config.systemPrompt.ifBlank { defaultSystemPrompt }))
        val allMessages = mutableListOf(systemMsg)
        messages.forEach { (role, content) ->
            allMessages.add(mapOf("role" to role, "content" to content))
        }

        val requestBody = mapOf(
            "model" to config.model,
            "messages" to allMessages,
            "temperature" to 0.7
        )

        val jsonBody = gson.toJson(requestBody)
        val request = Request.Builder()
            .url("${config.baseUrl.trimEnd('/')}/chat/completions")
            .addHeader("Authorization", "Bearer ${config.apiKey}")
            .addHeader("Content-Type", "application/json")
            .post(jsonBody.toRequestBody("application/json".toMediaType()))
            .build()

        try {
            val response = client.newCall(request).execute()
            response.use {
                val body = it.body?.string() ?: ""
                if (!it.isSuccessful) {
                    throw Exception("API 请求失败 (${it.code}): ${body.take(200)}")
                }
                val responseType = object : TypeToken<Map<String, Any>>() {}.type
                val json = gson.fromJson<Map<String, Any>>(body, responseType)
                    ?: throw Exception("AI 返回数据格式异常，无法解析")
                @Suppress("UNCHECKED_CAST")
                val choices = json["choices"] as? List<Map<String, Any>>
                    ?: throw Exception("AI 响应缺少 choices 字段")
                val message = choices.firstOrNull()?.get("message") as? Map<String, Any>
                    ?: throw Exception("AI 响应缺少 message 字段")
                val content = message["content"]
                when (content) {
                    is String -> content
                    is List<*> -> content.joinToString("") { it.toString() }
                    else -> ""
                }
            }
        } catch (e: SocketTimeoutException) {
            throw Exception("请求超时，请检查网络连接")
        } catch (e: UnknownHostException) {
            throw Exception("无法连接到服务器，请检查 Base URL")
        } catch (e: Exception) {
            if (e.message?.startsWith("API") == true || e.message?.startsWith("请求") == true ||
                e.message?.startsWith("无法") == true || e.message?.startsWith("AI") == true) {
                throw e
            }
            throw Exception("请求失败: ${e.message}")
        }
    }

    private fun mockResponse(messages: List<Pair<String, String>>): String {
        val lastUserMsg = messages.lastOrNull { it.first == "user" }?.second ?: ""

        val hasKeyword = fun(keywords: List<String>): Boolean {
            return keywords.any { lastUserMsg.contains(it) }
        }

        val analysis: String
        val tasks: List<Map<String, Any>>

        when {
            hasKeyword(listOf("学习", "课程", "考试", "复习", "读书", "阅读")) -> {
                analysis = "好的！我来帮你规划这个学习任务。根据你的目标，我拆分为以下几个阶段："
                tasks = listOf(
                    mapOf("title" to "制定学习计划", "description" to "明确学习目标和时间安排", "priority" to 2, "dueDaysFromNow" to 0, "children" to listOf(
                        mapOf("title" to "梳理知识大纲", "priority" to 2, "dueDaysFromNow" to 0),
                        mapOf("title" to "分配每日学习时间", "priority" to 2, "dueDaysFromNow" to 1)
                    )),
                    mapOf("title" to "基础概念学习", "description" to "系统学习核心知识点", "priority" to 1, "dueDaysFromNow" to 2, "children" to listOf(
                        mapOf("title" to "阅读教材重点章节", "priority" to 1, "dueDaysFromNow" to 2),
                        mapOf("title" to "做章节配套练习", "priority" to 1, "dueDaysFromNow" to 3),
                        mapOf("title" to "整理笔记和重点", "priority" to 2, "dueDaysFromNow" to 4)
                    )),
                    mapOf("title" to "实战练习", "description" to "通过习题巩固知识", "priority" to 1, "dueDaysFromNow" to 5, "children" to listOf(
                        mapOf("title" to "完成模拟题", "priority" to 1, "dueDaysFromNow" to 5),
                        mapOf("title" to "错题整理与复习", "priority" to 1, "dueDaysFromNow" to 6)
                    )),
                    mapOf("title" to "总复习与总结", "priority" to 2, "dueDaysFromNow" to 7)
                )
            }
            hasKeyword(listOf("项目", "开发", "产品", "上线", "需求", "设计")) -> {
                analysis = "好的！这是一个项目型任务，我按照标准项目流程为你拆解："
                tasks = listOf(
                    mapOf("title" to "需求分析与规划", "description" to "明确项目目标和范围", "priority" to 1, "dueDaysFromNow" to 0, "children" to listOf(
                        mapOf("title" to "收集用户需求", "priority" to 1, "dueDaysFromNow" to 0),
                        mapOf("title" to "确定功能优先级", "priority" to 2, "dueDaysFromNow" to 1),
                        mapOf("title" to "制定项目排期", "priority" to 2, "dueDaysFromNow" to 1)
                    )),
                    mapOf("title" to "设计阶段", "description" to "产品设计和技术方案", "priority" to 1, "dueDaysFromNow" to 2, "children" to listOf(
                        mapOf("title" to "原型设计", "priority" to 1, "dueDaysFromNow" to 2),
                        mapOf("title" to "技术方案评审", "priority" to 1, "dueDaysFromNow" to 3),
                        mapOf("title" to "UI 设计稿", "priority" to 2, "dueDaysFromNow" to 4)
                    )),
                    mapOf("title" to "开发实现", "description" to "核心功能开发", "priority" to 1, "dueDaysFromNow" to 5, "children" to listOf(
                        mapOf("title" to "搭建项目架构", "priority" to 1, "dueDaysFromNow" to 5),
                        mapOf("title" to "开发核心功能模块", "priority" to 1, "dueDaysFromNow" to 8),
                        mapOf("title" to "联调与测试", "priority" to 2, "dueDaysFromNow" to 12)
                    )),
                    mapOf("title" to "上线发布", "priority" to 2, "dueDaysFromNow" to 14)
                )
            }
            hasKeyword(listOf("健身", "减肥", "运动", "锻炼", "跑步")) -> {
                analysis = "好的！我来为你规划健身计划，循序渐进才能坚持下去："
                tasks = listOf(
                    mapOf("title" to "体能测试与目标设定", "priority" to 2, "dueDaysFromNow" to 0, "children" to listOf(
                        mapOf("title" to "测量身体基础数据", "priority" to 2, "dueDaysFromNow" to 0),
                        mapOf("title" to "设定阶段性目标", "priority" to 2, "dueDaysFromNow" to 0)
                    )),
                    mapOf("title" to "第一阶段：适应期（2周）", "description" to "培养运动习惯", "priority" to 1, "dueDaysFromNow" to 1, "children" to listOf(
                        mapOf("title" to "每周3次轻度有氧运动", "priority" to 1, "dueDaysFromNow" to 1),
                        mapOf("title" to "学习正确的动作姿势", "priority" to 2, "dueDaysFromNow" to 3),
                        mapOf("title" to "调整饮食结构", "priority" to 2, "dueDaysFromNow" to 5)
                    )),
                    mapOf("title" to "第二阶段：提升期（4周）", "description" to "增加训练强度", "priority" to 1, "dueDaysFromNow" to 14, "children" to listOf(
                        mapOf("title" to "加入力量训练", "priority" to 1, "dueDaysFromNow" to 14),
                        mapOf("title" to "提高有氧强度", "priority" to 1, "dueDaysFromNow" to 21)
                    ))
                )
            }
            else -> {
                analysis = "好的！我来帮你拆解这个任务。根据你的描述，我建议这样安排："
                tasks = listOf(
                    mapOf("title" to "明确目标与范围", "priority" to 1, "dueDaysFromNow" to 0, "children" to listOf(
                        mapOf("title" to "梳理具体需求", "priority" to 1, "dueDaysFromNow" to 0),
                        mapOf("title" to "确定优先级排序", "priority" to 2, "dueDaysFromNow" to 1)
                    )),
                    mapOf("title" to "制定执行计划", "priority" to 1, "dueDaysFromNow" to 2, "children" to listOf(
                        mapOf("title" to "拆分关键步骤", "priority" to 1, "dueDaysFromNow" to 2),
                        mapOf("title" to "预估每个步骤耗时", "priority" to 2, "dueDaysFromNow" to 3)
                    )),
                    mapOf("title" to "执行并定期复盘", "priority" to 1, "dueDaysFromNow" to 4)
                )
            }
        }

        val jsonStr = gson.toJson(mapOf("tasks" to tasks))
        return "$analysis\n\n```json\n$jsonStr\n```"
    }

    fun parseTasksFromResponse(content: String): List<Task> {
        // 优先尝试 JSON 格式解析（支持 ```json、```JSON、``` 等）
        val jsonMatch = Regex("""```(?:json|JSON)?\s*([\s\S]*?)\s*```""").find(content)
        if (jsonMatch != null) {
            try {
                val jsonStr = jsonMatch.groupValues[1]
                val responseType = object : TypeToken<Map<String, Any>>() {}.type
                val parsed = gson.fromJson<Map<String, Any>>(jsonStr, responseType)
                @Suppress("UNCHECKED_CAST")
                val rawTasks = parsed["tasks"] as? List<Map<String, Any>> ?: return emptyList()
                return convertJsonToTasks(rawTasks)
            } catch (e: Exception) {
                // JSON 解析失败，回退到正则
            }
        }
        // 尝试直接解析为 JSON
        val objStart = content.indexOf('{')
        val objEnd = content.lastIndexOf('}')
        if (objStart != -1 && objEnd != -1 && objEnd > objStart) {
            try {
                val jsonStr = content.substring(objStart, objEnd + 1)
                val responseType = object : TypeToken<Map<String, Any>>() {}.type
                val parsed = gson.fromJson<Map<String, Any>>(jsonStr, responseType)
                @Suppress("UNCHECKED_CAST")
                val rawTasks = parsed["tasks"] as? List<Map<String, Any>> ?: return emptyList()
                return convertJsonToTasks(rawTasks)
            } catch (e: Exception) {
                // 忽略
            }
        }
        // 回退到正则解析
        return parseTasksByRegex(content)
    }

    private fun convertJsonToTasks(rawTasks: List<Map<String, Any>>, parentId: Int? = null): List<Task> {
        val todayTs = System.currentTimeMillis()
        val dayMs = 24 * 60 * 60 * 1000L
        // 修复：使用 id=0 让数据库自动生成 ID，避免硬编码 ID 冲突
        return rawTasks.mapIndexed { index, item ->
            val priority = when ((item["priority"] as? Double)?.toInt() ?: 2) {
                1 -> TaskPriority.HIGH
                3 -> TaskPriority.LOW
                else -> TaskPriority.MEDIUM
            }
            val dueDays = (item["dueDaysFromNow"] as? Double)?.toInt() ?: 0

            @Suppress("UNCHECKED_CAST")
            val children = (item["children"] as? List<Map<String, Any>>)?.let { childList ->
                childList.mapIndexed { _, childItem ->
                    val childPriority = when ((childItem["priority"] as? Double)?.toInt() ?: 2) {
                        1 -> TaskPriority.HIGH
                        3 -> TaskPriority.LOW
                        else -> TaskPriority.MEDIUM
                    }
                    val childDueDays = (childItem["dueDaysFromNow"] as? Double)?.toInt() ?: 0
                    Task(
                        id = 0, // 让数据库自动生成
                        title = childItem["title"] as? String ?: "",
                        description = childItem["description"] as? String,
                        parentId = null, // 由 repository 在插入时回填
                        priority = childPriority,
                        dueDate = todayTs + childDueDays * dayMs,
                        aiGenerated = true,
                        children = emptyList()
                    )
                }
            } ?: emptyList()

            Task(
                id = 0, // 让数据库自动生成
                title = item["title"] as? String ?: "",
                description = item["description"] as? String,
                parentId = parentId,
                priority = priority,
                dueDate = todayTs + dueDays * dayMs,
                aiGenerated = true,
                children = children
            )
        }
    }

    private fun parseTasksByRegex(content: String): List<Task> {
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
