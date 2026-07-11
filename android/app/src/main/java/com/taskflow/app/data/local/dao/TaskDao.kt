package com.taskflow.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.taskflow.app.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks WHERE parentId IS NULL ORDER BY `order` ASC, createTime DESC")
    fun getAllParentTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE parentId = :parentId ORDER BY `order` ASC, createTime ASC")
    fun getChildrenByParentId(parentId: Int): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTaskById(id: Int): TaskEntity?

    @Query("SELECT * FROM tasks WHERE status = 'COMPLETED' ORDER BY updateTime DESC")
    fun getCompletedTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE dueDate IS NOT NULL AND dueDate BETWEEN :startOfDay AND :endOfDay AND parentId IS NULL ORDER BY `order` ASC")
    fun getTasksForDate(startOfDay: Long, endOfDay: Long): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'")
    fun searchTasks(query: String): Flow<List<TaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<TaskEntity>)

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTaskById(id: Int)

    @Query("DELETE FROM tasks WHERE parentId = :parentId")
    suspend fun deleteChildrenByParentId(parentId: Int)

    @Transaction
    suspend fun deleteTaskWithChildren(taskId: Int) {
        val task = getTaskById(taskId) ?: return
        val children = getChildrenList(taskId)
        children.forEach { child ->
            deleteTaskWithChildren(child.id)
        }
        deleteTask(task)
    }

    @Query("SELECT * FROM tasks WHERE parentId = :parentId")
    suspend fun getChildrenList(parentId: Int): List<TaskEntity>

    @Query("UPDATE tasks SET status = :status WHERE id = :id")
    suspend fun updateTaskStatus(id: Int, status: String)

    @Query("SELECT COUNT(*) FROM tasks WHERE parentId = :parentId AND status = 'COMPLETED'")
    suspend fun getCompletedChildrenCount(parentId: Int): Int

    @Query("SELECT COUNT(*) FROM tasks WHERE parentId = :parentId")
    suspend fun getTotalChildrenCount(parentId: Int): Int
}
