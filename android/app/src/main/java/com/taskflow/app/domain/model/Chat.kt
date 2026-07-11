package com.taskflow.app.domain.model

enum class ChatRole {
    USER,
    ASSISTANT
}

data class ChatMessage(
    val id: String,
    val role: ChatRole,
    val content: String,
    val timestamp: Long,
    val suggestedTasks: List<Task>? = null
)

data class Chat(
    val id: String,
    val title: String,
    val messages: List<ChatMessage> = emptyList(),
    val createTime: Long,
    val updateTime: Long
)

data class AiConfig(
    val provider: String = "openai",
    val baseUrl: String = "https://api.openai.com/v1",
    val apiKey: String = "",
    val model: String = "gpt-4o-mini",
    val systemPrompt: String = ""
)
