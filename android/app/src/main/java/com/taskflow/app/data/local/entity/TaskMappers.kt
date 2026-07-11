package com.taskflow.app.data.local.entity

import com.taskflow.app.domain.model.SyncStatus
import com.taskflow.app.domain.model.Task
import com.taskflow.app.domain.model.TaskPriority
import com.taskflow.app.domain.model.TaskStatus

fun TaskEntity.toDomain(children: List<Task> = emptyList()): Task {
    return Task(
        id = id,
        title = title,
        description = description,
        status = TaskStatus.valueOf(status),
        priority = TaskPriority.fromValue(priority),
        dueDate = dueDate,
        parentId = parentId,
        order = order,
        aiGenerated = aiGenerated,
        syncStatus = SyncStatus.valueOf(syncStatus),
        pomodoroCount = pomodoroCount,
        createTime = createTime,
        updateTime = updateTime,
        children = children
    )
}

fun Task.toEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        title = title,
        description = description,
        status = status.name,
        priority = priority.value,
        dueDate = dueDate,
        parentId = parentId,
        order = order,
        aiGenerated = aiGenerated,
        syncStatus = syncStatus.name,
        pomodoroCount = pomodoroCount,
        createTime = createTime,
        updateTime = updateTime
    )
}
