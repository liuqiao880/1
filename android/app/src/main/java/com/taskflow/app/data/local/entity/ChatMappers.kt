package com.taskflow.app.data.local.entity

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.taskflow.app.domain.model.Chat
import com.taskflow.app.domain.model.ChatMessage
import com.taskflow.app.domain.model.ChatRole
import com.taskflow.app.domain.model.Task

private val gson = Gson()
fun ChatEntity.toDomain(messages: List<ChatMessage> = emptyList()): Chat {
    return Chat(
        id = id,
        title = title,
        messages = messages,
        createTime = createTime,
        updateTime = updateTime
    )
}

fun Chat.toEntity(): ChatEntity {
    return ChatEntity(
        id = id,
        title = title,
        createTime = createTime,
        updateTime = updateTime
    )
}

fun ChatMessageEntity.toDomain(): ChatMessage {
    return ChatMessage(
        id = id,
        role = ChatRole.values().firstOrNull { it.name == role } ?: ChatRole.ASSISTANT,
        content = content,
        timestamp = timestamp,
        suggestedTasks = try {
            suggestedTasksJson?.let {
                gson.fromJson(it, object : TypeToken<List<Task>>() {}.type)
            }
        } catch (e: Exception) {
            null
        }
    )
}

fun ChatMessage.toEntity(chatId: String): ChatMessageEntity {
    return ChatMessageEntity(
        id = id,
        chatId = chatId,
        role = role.name,
        content = content,
        timestamp = timestamp,
        suggestedTasksJson = try {
            gson.toJson(suggestedTasks)
        } catch (e: Exception) {
            null
        }
    )
}
