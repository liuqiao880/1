package com.taskflow.app.ui.screen.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taskflow.app.domain.model.ChatMessage
import com.taskflow.app.domain.model.Task
import com.taskflow.app.domain.usecase.AddTasksUseCase
import com.taskflow.app.domain.usecase.CreateChatUseCase
import com.taskflow.app.domain.usecase.GetChatMessagesUseCase
import com.taskflow.app.domain.usecase.SendChatMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatDetailUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false,
    val chatTitle: String = "",
    val error: String? = null
)

@HiltViewModel
class ChatDetailViewModel @Inject constructor(
    private val getChatMessagesUseCase: GetChatMessagesUseCase,
    private val sendChatMessageUseCase: SendChatMessageUseCase,
    private val createChatUseCase: CreateChatUseCase,
    private val addTasksUseCase: AddTasksUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatDetailUiState())
    val uiState: StateFlow<ChatDetailUiState> = _uiState.asStateFlow()

    private var currentChatId: String? = null
    private var loadJob: Job? = null

    fun loadChat(chatId: String) {
        currentChatId = chatId
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            getChatMessagesUseCase(chatId).collectLatest { messages ->
                _uiState.value = _uiState.value.copy(messages = messages)
            }
        }
    }

    fun sendMessage(content: String) {
        val chatId = currentChatId ?: return
        if (content.isBlank() || _uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                sendChatMessageUseCase(chatId, content)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "发送失败，请检查网络和 API 配置"
                )
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun regenerateLastResponse() {
        val chatId = currentChatId ?: return
        val messages = _uiState.value.messages
        val lastUserIdx = messages.indexOfLast { it.role == com.taskflow.app.domain.model.ChatRole.USER }
        if (lastUserIdx == -1) return

        val userContent = messages[lastUserIdx].content

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                // 删除最后一条 assistant 消息后重新发送
                val truncated = messages.dropLastWhile { it.role != com.taskflow.app.domain.model.ChatRole.USER }
                // TODO: 需要 repository 支持删除最后一条消息后重发
                sendChatMessageUseCase(chatId, userContent)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "重新生成失败"
                )
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun createNewChat(onCreated: (String) -> Unit) {
        viewModelScope.launch {
            val chat = createChatUseCase()
            onCreated(chat.id)
        }
    }

    fun addTasksToTaskList(tasks: List<Task>) {
        viewModelScope.launch {
            addTasksUseCase(tasks)
        }
    }
}
