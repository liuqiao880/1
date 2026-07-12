package com.taskflow.app.ui.screen.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.taskflow.app.ui.components.AiPlanModal
import com.taskflow.app.ui.components.FilterTabs
import com.taskflow.app.ui.components.FloatingActionButton
import com.taskflow.app.ui.components.MultiSelectBar
import com.taskflow.app.ui.components.PomodoroPanel
import com.taskflow.app.ui.components.SettingsModal
import com.taskflow.app.ui.components.TaskEditModal
import com.taskflow.app.ui.components.TaskList
import com.taskflow.app.ui.components.TopAppBar
import com.taskflow.app.ui.components.UndoSnackbar

@Composable
fun HomeScreen(
    onNavigateToChatList: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                isSearchActive = uiState.isSearchActive,
                searchQuery = uiState.searchQuery,
                onSearchQueryChange = { viewModel.setSearchQuery(it) },
                onSearchActiveChange = { viewModel.setSearchActive(it) },
                onSettingsClick = { viewModel.setShowSettings(true) },
                onChatClick = onNavigateToChatList,
                multiSelectMode = uiState.multiSelectMode,
                selectedCount = uiState.selectedTasks.size,
                onCloseMultiSelect = { viewModel.toggleMultiSelect() },
                onDeleteSelected = { viewModel.deleteSelected() }
            )
            },
            floatingActionButton = {
                if (!uiState.multiSelectMode) {
                    FloatingActionButton(
                        onAddTask = { viewModel.openAddTask() },
                        onAiPlan = { viewModel.setShowAiModal(true) }
                    )
                }
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                FilterTabs(
                    filter = uiState.filter,
                    onFilterChange = { viewModel.setFilter(it) }
                )

                TaskList(
                    groupedTasks = uiState.groupedTasks,
                    expandedParents = uiState.expandedParents,
                    multiSelectMode = uiState.multiSelectMode,
                    selectedTasks = uiState.selectedTasks,
                    onTaskClick = { task ->
                        if (uiState.multiSelectMode) {
                            viewModel.toggleSelected(task.id)
                        } else if (task.children.isNotEmpty()) {
                            viewModel.toggleExpand(task.id)
                        } else {
                            viewModel.openEditTask(task)
                        }
                    },
                    onTaskChecked = { id ->
                        if (uiState.multiSelectMode) {
                            viewModel.toggleSelected(id)
                        } else {
                            viewModel.toggleTask(id)
                        }
                    },
                    onTaskLongPress = { task ->
                        if (!uiState.multiSelectMode) {
                            viewModel.toggleMultiSelect()
                            viewModel.toggleSelected(task.id)
                        }
                    },
                    onDeleteTask = { viewModel.deleteTask(it) },
                    onStartPomodoro = { task ->
                        viewModel.startPomodoro(task.id, task.title)
                    },
                    onChildClick = { task ->
                        if (uiState.multiSelectMode) {
                            viewModel.toggleSelected(task.id)
                        } else {
                            viewModel.openEditTask(task)
                        }
                    },
                    onChildChecked = { id ->
                        if (uiState.multiSelectMode) {
                            viewModel.toggleSelected(id)
                        } else {
                            viewModel.toggleTask(id)
                        }
                    }
                )

                if (uiState.undoMessage != null) {
                    UndoSnackbar(
                        message = uiState.undoMessage!!,
                        onUndo = { viewModel.undoDelete() },
                        onDismiss = { viewModel.clearUndoMessage() }
                    )
                }
            }
        }

        PomodoroPanel(
            state = uiState.pomodoroState,
            show = uiState.showPomodoroPanel,
            onDismiss = { viewModel.setShowPomodoroPanel(false) },
            onToggle = { viewModel.togglePomodoro() },
            onReset = { viewModel.resetPomodoro() },
            onSkip = { viewModel.skipPomodoro() },
            onSetPhase = { viewModel.setPomodoroPhase(it) },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        )
    }

    if (uiState.showAiModal) {
        AiPlanModal(
            onDismiss = { viewModel.setShowAiModal(false) },
            onConfirm = { tasks ->
                viewModel.addTasks(tasks)
                viewModel.setShowAiModal(false)
            },
            preferencesRepository = viewModel.preferencesRepository
        )
    }

    if (uiState.showEditModal && uiState.editingTask != null) {
        TaskEditModal(
            task = uiState.editingTask!!,
            onDismiss = { viewModel.closeEditTask() },
            onSave = { task ->
                if (uiState.editingTask?.id == 0) {
                    viewModel.addTask(task)
                } else {
                    viewModel.updateTask(task)
                }
                viewModel.closeEditTask()
            }
        )
    }

    if (uiState.showSettings) {
        SettingsModal(
            theme = uiState.theme,
            onThemeChange = { viewModel.setTheme(it) },
            dailyReminderEnabled = uiState.dailyReminderEnabled,
            onDailyReminderChange = { viewModel.setDailyReminderEnabled(it) },
            preferencesRepository = viewModel.preferencesRepository,
            onDismiss = { viewModel.setShowSettings(false) }
        )
    }
}
