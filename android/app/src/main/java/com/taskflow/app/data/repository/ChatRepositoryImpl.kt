package com.taskflow.app.data.repository

import com.taskflow.app.data.local.dao.ChatDao
import com.taskflow.app.data.local.entity.ChatEntity
import com.taskflow.app.data.local.entity.ChatMessageEntity
import com.taskflow.app.data.local.entity.toDomain
import com.taskflow.app.data.local.entity.toEntity
import com.taskflow.app.domain.model.Chat
import com.taskflow.app.domain.model.ChatMessage
import com.taskflow.app.domain.model.ChatRole
import com.taskflow.app.domain.repository.ChatRepository
import com.taskflow.app.domain.service.AiConfigData
import com.taskflow.app.domain.service.AiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.util.UUID
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatDao: ChatDao,
    private val aiService: AiService
) : ChatRepository {

    private val aiConfig = AiConfigData()

    override fun getAllChats(): Flow<List<Chat>> {
        return chatDao.getAllChats().combine(
            kotlinx.coroutines.flow.flowOf(emptyList<ChatMessage>())
        ) { chatEntities, _ ->
            chatEntities.map { it.toDomain() }
        }
    }

    override fun getChatMessages(chatId: String): Flow<List<ChatMessage>> {
        return chatDao.getMessagesForChat(chatId).let { flow ->
            flow.map { entities ->
                entities.map { it.toDomain() }
            }
        }
    }

    override suspend fun createChat(): Chat {
        val now = System.currentTimeMillis()
        val chatId = UUID.randomUUID().toString()
        val chat = Chat(
            id = chatId,
            title = "新对话",
            createTime = now,
            updateTime = now
        )
        chatDao.insertChat(chat.toEntity())
        return chat
    }

    override suspend fun deleteChat(chatId: String) {
        chatDao.deleteMessagesForChat(chatId)
        chatDao.deleteChat(chatId)
    }

    override suspend fun sendMessage(chatId: String, content: String): ChatMessage? {
        val now = System.currentTimeMillis()

        val chat = chatDao.getChatById(chatId) ?: return null
        val isFirst = chatDao.getMessageListForChat(chatId).isEmpty()

        val userMsgId = UUID.randomUUID().toString()
        val userMsg = ChatMessage(
            id = userMsgId,
            role = ChatRole.USER,
            content = content.trim(),
            timestamp = now
        )
        chatDao.insertMessage(userMsg.toEntity(chatId))

        if (isFirst) {
            val title = content.take(20)
            chatDao.updateChatTitle(chatId, title, now)
        } else {
            chatDao.updateChatTime(chatId, now)
        }

        val history = chatDao.getMessageListForChat(chatId).map {
            (if (it.role == "USER") "user" else "assistant") to it.content
        }

        val response = aiService.chat(history, aiConfig)
        val suggestedTasks = aiService.parseTasksFromResponse(response)

        val assistantMsgId = UUID.randomUUID().toString()
        val assistantMsg = ChatMessage(
            id = assistantMsgId,
            role = ChatRole.ASSISTANT,
            content = response,
            timestamp = System.currentTimeMillis(),
            suggestedTasks = suggestedTasks.ifEmpty { null }
        )
        chatDao.insertMessage(assistantMsg.toEntity(chatId))
        chatDao.updateChatTime(chatId, System.currentTimeMillis())

        return assistantMsg
    }

    override suspend fun updateChatTitle(chatId: String, title: String) {
        chatDao.updateChatTitle(chatId, title, System.currentTimeMillis())
    }
}
