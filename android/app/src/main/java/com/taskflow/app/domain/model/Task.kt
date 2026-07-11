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

enum class PomodoroPhase {
    FOCUS,
    SHORT_BREAK,
    LONG_BREAK
}

data class PomodoroState(
    val isRunning: Boolean = false,
    val phase: PomodoroPhase = PomodoroPhase.FOCUS,
    val timeRemaining: Long = 25 * 60 * 1000L,
    val currentTaskId: Int? = null,
    val currentTaskTitle: String = "",
    val completedPomodoros: Int = 0,
    val focusDuration: Long = 25 * 60 * 1000L,
    val shortBreakDuration: Long = 5 * 60 * 1000L,
    val longBreakDuration: Long = 15 * 60 * 1000L,
    val longBreakInterval: Int = 4
)

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
    val pomodoroCount: Int = 0,
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
