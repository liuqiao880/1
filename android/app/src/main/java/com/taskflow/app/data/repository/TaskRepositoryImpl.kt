package com.taskflow.app.data.repository

import com.taskflow.app.data.local.dao.TaskDao
import com.taskflow.app.data.local.entity.TaskEntity
import com.taskflow.app.data.local.entity.toDomain
import com.taskflow.app.data.local.entity.toEntity
import com.taskflow.app.domain.model.FilterType
import com.taskflow.app.domain.model.Task
import com.taskflow.app.domain.model.TaskStatus
import com.taskflow.app.domain.repository.TaskRepository
import com.taskflow.app.util.DateUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao
) : TaskRepository {

    override fun getAllTasks(): Flow<List<Task>> {
        return taskDao.getAllParentTasks().map { parentList ->
            parentList.map { parent ->
                buildTaskWithChildren(parent)
            }
        }
    }

    override fun getParentTasks(): Flow<List<Task>> = getAllTasks()

    override fun getTaskById(id: Int): Flow<Task?> {
        return taskDao.getAllParentTasks().map { list ->
            list.find { it.id == id }?.let { buildTaskWithChildren(it) }
        }
    }

    override fun getTasksByDate(startOfDay: Long, endOfDay: Long): Flow<List<Task>> {
        return taskDao.getTasksForDate(startOfDay, endOfDay).map { list ->
            list.map { buildTaskWithChildren(it) }
        }
    }

    override fun searchTasks(query: String): Flow<List<Task>> {
        return taskDao.searchTasks(query).map { list ->
            list.filter { it.parentId == null }.map { buildTaskWithChildren(it) }
        }
    }

    override suspend fun insertTask(task: Task): Long {
        val entity = task.toEntity()
        val id = taskDao.insertTask(entity)
        task.children.forEach { child ->
            taskDao.insertTask(child.copy(parentId = id.toInt()).toEntity())
        }
        return id
    }

    override suspend fun insertTasks(tasks: List<Task>) {
        tasks.forEach { insertTask(it) }
    }

    override suspend fun updateTask(task: Task) {
        taskDao.updateTask(task.copy(updateTime = System.currentTimeMillis()).toEntity())
    }

    override suspend fun deleteTask(id: Int) {
        taskDao.deleteTaskWithChildren(id)
    }

    override suspend fun toggleTaskStatus(id: Int) {
        val task = taskDao.getTaskById(id) ?: return
        val newStatus = if (task.status == TaskStatus.COMPLETED.name) {
            TaskStatus.TODO.name
        } else {
            TaskStatus.COMPLETED.name
        }
        taskDao.updateTaskStatus(id, newStatus)
    }

    override suspend fun getProgress(taskId: Int): Float {
        val total = taskDao.getTotalChildrenCount(taskId)
        if (total == 0) return 0f
        val completed = taskDao.getCompletedChildrenCount(taskId)
        return completed.toFloat() / total
    }

    override fun getGroupedTasks(filter: FilterType): Flow<List<Pair<String, List<Task>>>> {
        return getAllTasks().map { tasks ->
            val filtered = when (filter) {
                FilterType.ALL -> tasks
                FilterType.TODAY -> tasks.filter {
                    it.dueDate?.let { date -> DateUtils.isToday(date) } == true
                }
                FilterType.TOMORROW -> tasks.filter {
                    it.dueDate?.let { date -> DateUtils.isTomorrow(date) } == true
                }
                FilterType.THIS_WEEK -> tasks.filter {
                    it.dueDate?.let { date -> DateUtils.isThisWeek(date) } == true
                }
            }
            DateUtils.groupTasksByDate(filtered)
        }
    }

    private suspend fun buildTaskWithChildren(parent: TaskEntity): Task {
        val children = taskDao.getChildrenList(parent.id).map { it.toDomain() }
        return parent.toDomain(children)
    }
}
