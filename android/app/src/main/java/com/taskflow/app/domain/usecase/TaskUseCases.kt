package com.taskflow.app.domain.usecase

import com.taskflow.app.domain.model.Task
import com.taskflow.app.domain.repository.TaskRepository
import javax.inject.Inject

class ToggleTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(taskId: Int) {
        repository.toggleTaskStatus(taskId)
    }
}

class AddTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(task: Task): Long {
        return repository.insertTask(task)
    }
}

class AddTasksUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(tasks: List<Task>) {
        repository.insertTasks(tasks)
    }
}

class UpdateTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(task: Task) {
        repository.updateTask(task)
    }
}

class DeleteTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(taskId: Int) {
        repository.deleteTask(taskId)
    }
}

class GetTaskByIdUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    operator fun invoke(taskId: Int) = repository.getTaskById(taskId)
}

class GetGroupedTasksUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    operator fun invoke(filter: com.taskflow.app.domain.model.FilterType) =
        repository.getGroupedTasks(filter)
}

class SearchTasksUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    operator fun invoke(query: String) = repository.searchTasks(query)
}

class IncrementPomodoroUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(taskId: Int) {
        repository.incrementPomodoroCount(taskId)
    }
}
