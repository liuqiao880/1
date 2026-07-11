package com.taskflow.app.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taskflow.app.domain.model.FilterType
import com.taskflow.app.domain.model.Task
import com.taskflow.app.domain.model.ThemeType
import com.taskflow.app.domain.repository.PreferencesRepository
import com.taskflow.app.domain.usecase.AddTaskUseCase
import com.taskflow.app.domain.usecase.AddTasksUseCase
import com.taskflow.app.domain.usecase.DeleteTaskUseCase
import com.taskflow.app.domain.usecase.GetGroupedTasksUseCase
import com.taskflow.app.domain.usecase.GetTaskByIdUseCase
import com.taskflow.app.domain.usecase.SearchTasksUseCase
import com.taskflow.app.domain.usecase.ToggleTaskUseCase
import com.taskflow.app.domain.usecase.UpdateTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val tasks: List<Task> = emptyList(),
    val groupedTasks: List<Pair<String, List<Task>>> = emptyList(),
    val filter: FilterType = FilterType.ALL,
    val searchQuery: String = "",
    val isSearchActive: Boolean = false,
    val expandedParents: Set<Int> = emptySet(),
    val theme: ThemeType = ThemeType.SYSTEM,
    val showAiModal: Boolean = false,
    val showEditModal: Boolean = false,
    val editingTask: Task? = null,
    val multiSelectMode: Boolean = false,
    val selectedTasks: Set<Int> = emptySet(),
    val isLoading: Boolean = false,
    val undoMessage: String? = null,
    val showSettings: Boolean = false,
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getGroupedTasksUseCase: GetGroupedTasksUseCase,
    private val toggleTaskUseCase: ToggleTaskUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val addTasksUseCase: AddTasksUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val searchTasksUseCase: SearchTasksUseCase,
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadTasks()
        observePreferences()
    }

    private fun loadTasks() {
        viewModelScope.launch {
            combine(
                getGroupedTasksUseCase(_uiState.value.filter),
                preferencesRepository.expandedParents
            ) { grouped, expanded ->
                _uiState.value.copy(
                    groupedTasks = grouped,
                    expandedParents = expanded
                )
            }.collectLatest { state ->
                _uiState.value = state
            }
        }
    }

    private fun observePreferences() {
        viewModelScope.launch {
            preferencesRepository.theme.collectLatest { theme ->
                _uiState.value = _uiState.value.copy(theme = theme)
            }
        }
    }

    fun setFilter(filter: FilterType) {
        _uiState.value = _uiState.value.copy(filter = filter)
        loadTasks()
    }

    fun toggleTask(taskId: Int) {
        viewModelScope.launch {
            toggleTaskUseCase(taskId)
        }
    }

    fun toggleExpand(taskId: Int) {
        viewModelScope.launch {
            preferencesRepository.toggleParentExpanded(taskId)
        }
    }

    fun setSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        if (query.isNotBlank()) {
            viewModelScope.launch {
                searchTasksUseCase(query).collectLatest { tasks ->
                    val grouped = if (tasks.isEmpty()) {
                        emptyList()
                    } else {
                        listOf("搜索结果" to tasks)
                    }
                    _uiState.value = _uiState.value.copy(groupedTasks = grouped)
                }
            }
        } else {
            loadTasks()
        }
    }

    fun setSearchActive(active: Boolean) {
        _uiState.value = _uiState.value.copy(isSearchActive = active)
        if (!active) {
            _uiState.value = _uiState.value.copy(searchQuery = "")
            loadTasks()
        }
    }

    fun setShowAiModal(show: Boolean) {
        _uiState.value = _uiState.value.copy(showAiModal = show)
    }

    fun openEditTask(task: Task) {
        _uiState.value = _uiState.value.copy(
            showEditModal = true,
            editingTask = task
        )
    }

    fun closeEditTask() {
        _uiState.value = _uiState.value.copy(
            showEditModal = false,
            editingTask = null
        )
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            updateTaskUseCase(task)
            closeEditTask()
        }
    }

    fun deleteTask(taskId: Int) {
        viewModelScope.launch {
            deleteTaskUseCase(taskId)
            _uiState.value = _uiState.value.copy(undoMessage = "任务已删除")
        }
    }

    fun clearUndoMessage() {
        _uiState.value = _uiState.value.copy(undoMessage = null)
    }

    fun toggleMultiSelect() {
        _uiState.value = _uiState.value.copy(
            multiSelectMode = !_uiState.value.multiSelectMode,
            selectedTasks = if (_uiState.value.multiSelectMode) emptySet() else _uiState.value.selectedTasks
        )
    }

    fun toggleSelected(taskId: Int) {
        val current = _uiState.value.selectedTasks
        _uiState.value = _uiState.value.copy(
            selectedTasks = if (current.contains(taskId)) {
                current - taskId
            } else {
                current + taskId
            }
        )
    }

    fun deleteSelected() {
        viewModelScope.launch {
            val count = _uiState.value.selectedTasks.size
            _uiState.value.selectedTasks.forEach { id ->
                deleteTaskUseCase(id)
            }
            _uiState.value = _uiState.value.copy(
                multiSelectMode = false,
                selectedTasks = emptySet(),
                undoMessage = "已删除 $count 个任务"
            )
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch {
            addTaskUseCase(task)
        }
    }

    fun addTasks(tasks: List<Task>) {
        viewModelScope.launch {
            addTasksUseCase(tasks)
        }
    }

    fun setShowSettings(show: Boolean) {
        _uiState.value = _uiState.value.copy(showSettings = show)
    }

    fun setTheme(theme: ThemeType) {
        viewModelScope.launch {
            preferencesRepository.setTheme(theme)
        }
    }
}
