package com.taskflow.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "chats")
data class ChatEntity(
    @PrimaryKey val id: String,
    val title: String,
    val createTime: Long,
    val updateTime: Long
)

@Entity(
    tableName = "chat_messages",
    foreignKeys = [ForeignKey(
        entity = ChatEntity::class,
        parentColumns = ["id"],
        childColumns = ["chatId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("chatId")]
)
data class ChatMessageEntity(
    @PrimaryKey val id: String,
    val chatId: String,
    val role: String,
    val content: String,
    val timestamp: Long,
    val suggestedTasksJson: String? = null
)
