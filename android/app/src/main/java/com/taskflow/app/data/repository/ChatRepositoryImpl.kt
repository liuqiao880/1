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
            val title = if (content.length > 20) content.take(20) + "..." else content
            chatDao.updateChatTitle(chatId, title, now)
        } else {
            chatDao.updateChatTime(chatId, now)
        }

        // 构建调用 AI 的历史：限制最近 20 条避免超出 token 上限
        val recentMessages = existingMessages.takeLast(20)
        val history = recentMessages.map {
            (if (it.role == "USER") "user" else "assistant") to it.content
        } + ("user" to content.trim())

        // 修复：补全 provider 和 systemPrompt
        val aiConfig = AiConfigData(
            provider = preferencesRepository.aiProvider.first(),
            baseUrl = preferencesRepository.aiBaseUrl.first(),
            apiKey = preferencesRepository.aiApiKey.first(),
            model = preferencesRepository.aiModel.first(),
            systemPrompt = preferencesRepository.aiSystemPrompt.first()
        )

        val response = try {
            aiService.chat(history, aiConfig)
        } catch (e: Exception) {
            // 修复：保留原始错误信息
            "⚠️ AI 服务调用失败：${e.message}\n\n请检查网络连接和 API 配置后重试。"
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

    override suspend fun deleteMessagesFrom(chatId: String, timestamp: Long) {
        chatDao.deleteMessagesFrom(chatId, timestamp)
    }

    override suspend fun getChatById(chatId: String): Chat? {
        val entity = chatDao.getChatById(chatId) ?: return null
        return entity.toDomain(emptyList())
    }
}
