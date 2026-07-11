package com.taskflow.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.taskflow.app.domain.model.TaskPriority
import com.taskflow.app.domain.model.TaskStatus
import com.taskflow.app.domain.model.SyncStatus

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String? = null,
    val status: String = TaskStatus.TODO.name,
    val priority: Int = TaskPriority.MEDIUM.value,
    val dueDate: Long? = null,
    val parentId: Int? = null,
    val order: Int = 0,
    val aiGenerated: Boolean = false,
    val syncStatus: String = SyncStatus.LOCAL_ONLY.name,
    val createTime: Long = System.currentTimeMillis(),
    val updateTime: Long = System.currentTimeMillis(),
    val extra1: String? = null,
    val extra2: Int? = null,
)
