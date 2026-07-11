package com.taskflow.app.domain.model

enum class TaskStatus {
    TODO,
    COMPLETED,
    IN_PROGRESS,
    CANCELLED
}

enum class TaskPriority(val value: Int) {
    HIGH(1),
    MEDIUM(2),
    LOW(3);

    companion object {
        fun fromValue(value: Int): TaskPriority {
            return values().firstOrNull { it.value == value } ?: MEDIUM
        }
    }
}

enum class SyncStatus {
    LOCAL_ONLY,
    SYNCED,
    PENDING
}

enum class FilterType {
    ALL,
    TODAY,
    TOMORROW,
    THIS_WEEK
}

enum class ThemeType {
    LIGHT,
    DARK,
    SYSTEM
}

data class Task(
    val id: Int = 0,
    val title: String,
    val description: String? = null,
    val status: TaskStatus = TaskStatus.TODO,
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val dueDate: Long? = null,
    val parentId: Int? = null,
    val order: Int = 0,
    val aiGenerated: Boolean = false,
    val syncStatus: SyncStatus = SyncStatus.LOCAL_ONLY,
    val createTime: Long = System.currentTimeMillis(),
    val updateTime: Long = System.currentTimeMillis(),
    val children: List<Task> = emptyList()
) {
    val progress: Float
        get() {
            if (children.isEmpty()) return if (status == TaskStatus.COMPLETED) 1f else 0f
            val completed = children.count { it.status == TaskStatus.COMPLETED }
            return completed.toFloat() / children.size
        }
}

data class TaskGroup(
    val key: String,
    val label: String,
    val tasks: List<Task>
)
