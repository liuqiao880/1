package com.taskflow.app.data.local.entity

import com.taskflow.app.domain.model.Chat
import com.taskflow.app.domain.model.ChatMessage
import com.taskflow.app.domain.model.ChatRole

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
        role = ChatRole.valueOf(role),
        content = content,
        timestamp = timestamp,
        suggestedTasks = null
    )
}

fun ChatMessage.toEntity(chatId: String): ChatMessageEntity {
    return ChatMessageEntity(
        id = id,
        chatId = chatId,
        role = role.name,
        content = content,
        timestamp = timestamp,
        suggestedTasksJson = null
    )
}
