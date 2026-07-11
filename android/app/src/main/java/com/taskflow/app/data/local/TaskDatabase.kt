package com.taskflow.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.taskflow.app.data.local.dao.ChatDao
import com.taskflow.app.data.local.dao.TaskDao
import com.taskflow.app.data.local.entity.ChatEntity
import com.taskflow.app.data.local.entity.ChatMessageEntity
import com.taskflow.app.data.local.entity.TaskEntity

@Database(
    entities = [TaskEntity::class, ChatEntity::class, ChatMessageEntity::class],
    version = 3,
    exportSchema = true
)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun chatDao(): ChatDao

    companion object {
        const val DATABASE_NAME = "taskflow_db"
    }
}
