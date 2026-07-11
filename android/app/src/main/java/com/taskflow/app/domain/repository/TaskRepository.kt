package com.taskflow.app.domain.repository

import com.taskflow.app.domain.model.FilterType
import com.taskflow.app.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getAllTasks(): Flow<List<Task>>
    fun getParentTasks(): Flow<List<Task>>
    fun getTaskById(id: Int): Flow<Task?>
    fun getTasksByDate(startOfDay: Long, endOfDay: Long): Flow<List<Task>>
    fun searchTasks(query: String): Flow<List<Task>>
    suspend fun insertTask(task: Task): Long
    suspend fun insertTasks(tasks: List<Task>)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(id: Int)
    suspend fun toggleTaskStatus(id: Int)
    suspend fun incrementPomodoroCount(taskId: Int)
    suspend fun getProgress(taskId: Int): Float
    fun getGroupedTasks(filter: FilterType = FilterType.ALL): Flow<List<Pair<String, List<Task>>>>
}
