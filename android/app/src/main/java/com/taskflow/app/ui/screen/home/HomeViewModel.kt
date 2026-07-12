package com.taskflow.app.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taskflow.app.domain.model.FilterType
import com.taskflow.app.domain.model.PomodoroPhase
import com.taskflow.app.domain.model.PomodoroState
import com.taskflow.app.domain.model.Task
import com.taskflow.app.domain.model.TaskPriority
import com.taskflow.app.domain.model.TaskStatus
import com.taskflow.app.domain.model.ThemeType
import com.taskflow.app.domain.repository.PreferencesRepository
import com.taskflow.app.domain.usecase.AddTaskUseCase
import com.taskflow.app.domain.usecase.AddTasksUseCase
import com.taskflow.app.domain.usecase.DeleteTaskUseCase
import com.taskflow.app.domain.usecase.GetGroupedTasksUseCase
import com.taskflow.app.domain.usecase.GetTaskByIdUseCase
import com.taskflow.app.domain.usecase.IncrementPomodoroUseCase
import com.taskflow.app.domain.usecase.SearchTasksUseCase
import com.taskflow.app.domain.usecase.ToggleTaskUseCase
import com.taskflow.app.domain.usecase.UpdateTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
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
    val dailyReminderEnabled: Boolean = true,
    val showAiModal: Boolean = false,
    val showEditModal: Boolean = false,
    val editingTask: Task? = null,
    val multiSelectMode: Boolean = false,
    val selectedTasks: Set<Int> = emptySet(),
    val isLoading: Boolean = false,
    val undoMessage: String? = null,
    val showSettings: Boolean = false,
    val pomodoroState: PomodoroState = PomodoroState(),
    val showPomodoroPanel: Boolean = false,
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
    private val incrementPomodoroUseCase: IncrementPomodoroUseCase,
    val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private var timerJob: Job? = null
    private var loadJob: Job? = null
    private var searchJob: Job? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    private val filterFlow = MutableStateFlow(FilterType.ALL)

    private var lastDeletedTask: Task? = null
    private var lastDeletedTasks: List<Task> = emptyList()

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        observeTasks()
        observePreferences()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeTasks() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            filterFlow.flatMapLatest { filter ->
                combine(
                    getGroupedTasksUseCase(filter),
                    preferencesRepository.expandedParents
                ) { grouped, expanded ->
                    _uiState.value.copy(
                        groupedTasks = grouped,
                        expandedParents = expanded,
                        filter = filter
                    )
                }
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
        viewModelScope.launch {
            preferencesRepository.dailyReminderEnabled.collectLatest { enabled ->
                _uiState.value = _uiState.value.copy(dailyReminderEnabled = enabled)
            }
        }
    }

    fun setDailyReminderEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setDailyReminderEnabled(enabled)
        }
    }

    fun setFilter(filter: FilterType) {
        filterFlow.value = filter
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
        searchJob?.cancel()
        if (query.isNotBlank()) {
            searchJob = viewModelScope.launch {
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
            observeTasks()
        }
    }

    fun setSearchActive(active: Boolean) {
        _uiState.value = _uiState.value.copy(isSearchActive = active)
        if (!active) {
            _uiState.value = _uiState.value.copy(searchQuery = "")
            searchJob?.cancel()
            observeTasks()
        }
    }

    fun setShowAiModal(show: Boolean) {
        _uiState.value = _uiState.value.copy(showAiModal = show)
    }

    fun openAddTask() {
        _uiState.value = _uiState.value.copy(
            showEditModal = true,
            editingTask = Task(
                id = 0,
                title = "",
                description = "",
                status = TaskStatus.TODO,
                priority = TaskPriority.MEDIUM,
                dueDate = System.currentTimeMillis(),
                createTime = System.currentTimeMillis(),
                updateTime = System.currentTimeMillis()
            )
        )
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
            val task = getTaskByIdUseCase(taskId).first()
            lastDeletedTask = task
            lastDeletedTasks = emptyList()
            deleteTaskUseCase(taskId)
            _uiState.value = _uiState.value.copy(undoMessage = "任务已删除")
        }
    }

    fun undoDelete() {
        viewModelScope.launch {
            if (lastDeletedTasks.isNotEmpty()) {
                lastDeletedTasks.forEach { addTaskUseCase(it) }
                lastDeletedTasks = emptyList()
            } else {
                lastDeletedTask?.let { task ->
                    addTaskUseCase(task)
                    lastDeletedTask = null
                }
            }
            _uiState.value = _uiState.value.copy(undoMessage = null)
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
            val selectedIds = _uiState.value.selectedTasks
            val deleted = mutableListOf<Task>()
            selectedIds.forEach { id ->
                val task = getTaskByIdUseCase(id).first()
                if (task != null) deleted.add(task)
                deleteTaskUseCase(id)
            }
            lastDeletedTasks = deleted
            lastDeletedTask = null
            _uiState.value = _uiState.value.copy(
                multiSelectMode = false,
                selectedTasks = emptySet(),
                undoMessage = "已删除 ${selectedIds.size} 个任务"
            )
        }
    }

    fun undoDeleteSelected() {
        viewModelScope.launch {
            lastDeletedTasks.forEach { addTaskUseCase(it) }
            lastDeletedTasks = emptyList()
            _uiState.value = _uiState.value.copy(undoMessage = null)
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

    fun startPomodoro(taskId: Int, taskTitle: String) {
        val current = _uiState.value.pomodoroState
        _uiState.value = _uiState.value.copy(
            showPomodoroPanel = true,
            pomodoroState = current.copy(
                isRunning = true,
                currentTaskId = taskId,
                currentTaskTitle = taskTitle,
                phase = PomodoroPhase.FOCUS,
                timeRemaining = current.focusDuration
            )
        )
        startTimer()
    }

    fun togglePomodoro() {
        val current = _uiState.value.pomodoroState
        val newRunning = !current.isRunning
        _uiState.value = _uiState.value.copy(
            pomodoroState = current.copy(isRunning = newRunning)
        )
        if (newRunning) {
            startTimer()
        } else {
            stopTimer()
        }
    }

    fun resetPomodoro() {
        stopTimer()
        val current = _uiState.value.pomodoroState
        val duration = when (current.phase) {
            PomodoroPhase.FOCUS -> current.focusDuration
            PomodoroPhase.SHORT_BREAK -> current.shortBreakDuration
            PomodoroPhase.LONG_BREAK -> current.longBreakDuration
        }
        _uiState.value = _uiState.value.copy(
            pomodoroState = current.copy(
                isRunning = false,
                timeRemaining = duration
            )
        )
    }

    fun skipPomodoro() {
        moveToNextPhase()
    }

    fun setPomodoroPhase(phase: PomodoroPhase) {
        stopTimer()
        val current = _uiState.value.pomodoroState
        val duration = when (phase) {
            PomodoroPhase.FOCUS -> current.focusDuration
            PomodoroPhase.SHORT_BREAK -> current.shortBreakDuration
            PomodoroPhase.LONG_BREAK -> current.longBreakDuration
        }
        _uiState.value = _uiState.value.copy(
            pomodoroState = current.copy(
                phase = phase,
                timeRemaining = duration,
                isRunning = false
            )
        )
    }

    fun setShowPomodoroPanel(show: Boolean) {
        _uiState.value = _uiState.value.copy(showPomodoroPanel = show)
    }

    private fun startTimer() {
        stopTimer()
        timerJob = viewModelScope.launch {
            while (_uiState.value.pomodoroState.isRunning && _uiState.value.pomodoroState.timeRemaining > 0) {
                delay(1000L)
                val current = _uiState.value.pomodoroState
                if (current.isRunning) {
                    _uiState.value = _uiState.value.copy(
                        pomodoroState = current.copy(
                            timeRemaining = (current.timeRemaining - 1000L).coerceAtLeast(0L)
                        )
                    )
                }
            }
            if (_uiState.value.pomodoroState.timeRemaining <= 0L && _uiState.value.pomodoroState.isRunning) {
                onPhaseComplete()
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    private suspend fun onPhaseComplete() {
        stopTimer()
        val current = _uiState.value.pomodoroState
        if (current.phase == PomodoroPhase.FOCUS && current.currentTaskId != null) {
            incrementPomodoroUseCase(current.currentTaskId)
        }
        val newCompleted = if (current.phase == PomodoroPhase.FOCUS) current.completedPomodoros + 1 else current.completedPomodoros
        val nextPhase = when (current.phase) {
            PomodoroPhase.FOCUS -> if (newCompleted > 0 && newCompleted % current.longBreakInterval == 0) PomodoroPhase.LONG_BREAK else PomodoroPhase.SHORT_BREAK
            PomodoroPhase.SHORT_BREAK, PomodoroPhase.LONG_BREAK -> PomodoroPhase.FOCUS
        }
        val nextDuration = when (nextPhase) {
            PomodoroPhase.FOCUS -> current.focusDuration
            PomodoroPhase.SHORT_BREAK -> current.shortBreakDuration
            PomodoroPhase.LONG_BREAK -> current.longBreakDuration
        }
        _uiState.value = _uiState.value.copy(
            pomodoroState = current.copy(
                phase = nextPhase,
                timeRemaining = nextDuration,
                isRunning = false,
                completedPomodoros = newCompleted
            )
        )
    }

    private fun moveToNextPhase() {
        stopTimer()
        val current = _uiState.value.pomodoroState
        val nextPhase = when (current.phase) {
            PomodoroPhase.FOCUS -> PomodoroPhase.SHORT_BREAK
            PomodoroPhase.SHORT_BREAK -> PomodoroPhase.FOCUS
            PomodoroPhase.LONG_BREAK -> PomodoroPhase.FOCUS
        }
        val nextDuration = when (nextPhase) {
            PomodoroPhase.FOCUS -> current.focusDuration
            PomodoroPhase.SHORT_BREAK -> current.shortBreakDuration
            PomodoroPhase.LONG_BREAK -> current.longBreakDuration
        }
        _uiState.value = _uiState.value.copy(
            pomodoroState = current.copy(
                phase = nextPhase,
                timeRemaining = nextDuration,
                isRunning = false
            )
        )
    }
}
