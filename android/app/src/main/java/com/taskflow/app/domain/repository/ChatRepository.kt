package com.taskflow.app.domain.repository

import com.taskflow.app.domain.model.Chat
import com.taskflow.app.domain.model.ChatMessage
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getAllChats(): Flow<List<Chat>>
    fun getChatMessages(chatId: String): Flow<List<ChatMessage>>
    suspend fun createChat(): Chat
    suspend fun deleteChat(chatId: String)
    suspend fun sendMessage(chatId: String, content: String): ChatMessage?
    suspend fun updateChatTitle(chatId: String, title: String)
}
