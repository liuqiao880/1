package com.taskflow.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.taskflow.app.data.local.entity.ChatEntity
import com.taskflow.app.data.local.entity.ChatMessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {

    @Query("SELECT * FROM chats ORDER BY updateTime DESC")
    fun getAllChats(): Flow<List<ChatEntity>>

    @Query("SELECT * FROM chats WHERE id = :id")
    suspend fun getChatById(id: String): ChatEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChat(chat: ChatEntity)

    @Query("DELETE FROM chats WHERE id = :id")
    suspend fun deleteChat(id: String)

    @Query("UPDATE chats SET title = :title, updateTime = :updateTime WHERE id = :id")
    suspend fun updateChatTitle(id: String, title: String, updateTime: Long)

    @Query("UPDATE chats SET updateTime = :updateTime WHERE id = :id")
    suspend fun updateChatTime(id: String, updateTime: Long)

    @Query("SELECT * FROM chat_messages WHERE chatId = :chatId ORDER BY timestamp ASC")
    fun getMessagesForChat(chatId: String): Flow<List<ChatMessageEntity>>

    @Query("SELECT * FROM chat_messages WHERE chatId = :chatId ORDER BY timestamp ASC")
    suspend fun getMessageListForChat(chatId: String): List<ChatMessageEntity>

    @Query("SELECT * FROM chat_messages WHERE chatId = :chatId ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestMessage(chatId: String): ChatMessageEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessageEntity)

    @Query("DELETE FROM chat_messages WHERE chatId = :chatId")
    suspend fun deleteMessagesForChat(chatId: String)
}
