package com.taskflow.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.taskflow.app.data.local.dao.TaskDao
import com.taskflow.app.data.local.entity.TaskEntity

@Database(
    entities = [TaskEntity::class],
    version = 2,
    exportSchema = true
)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        const val DATABASE_NAME = "taskflow_db"
    }
}
