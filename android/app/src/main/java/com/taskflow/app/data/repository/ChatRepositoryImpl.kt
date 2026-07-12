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
import com.taskflow.app.domain.repository.PreferencesRepository
import com.taskflow.app.domain.service.AiConfigData
import com.taskflow.app.domain.service.AiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val chatDao: ChatDao,
    private val aiService: AiService,
    private val preferencesRepository: PreferencesRepository
) : ChatRepository {

    override fun getAllChats(): Flow<List<Chat>> {
        return chatDao.getAllChats().map { chatEntities ->
            chatEntities.map { entity ->
                val latestMsg = chatDao.getLatestMessage(entity.id)
                val messages = if (latestMsg != null) listOf(latestMsg.toDomain()) else emptyList()
                entity.toDomain(messages)
            }
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

        // 获取历史消息（不包含当前用户消息），同时判断是否为首条消息
        val existingMessages = chatDao.getMessageListForChat(chatId)
        val isFirst = existingMessages.isEmpty()

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

        // 构建调用 AI 的历史：既有消息 + 当前用户消息
        val history = existingMessages.map {
            (if (it.role == "USER") "user" else "assistant") to it.content
        } + ("user" to content.trim())

        val aiConfig = AiConfigData(
            apiKey = preferencesRepository.aiApiKey.first(),
            baseUrl = preferencesRepository.aiBaseUrl.first(),
            model = preferencesRepository.aiModel.first()
        )

        val response = try {
            aiService.chat(history, aiConfig)
        } catch (e: Exception) {
            "抱歉，AI 服务暂时不可用，请稍后重试。"
        }
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
