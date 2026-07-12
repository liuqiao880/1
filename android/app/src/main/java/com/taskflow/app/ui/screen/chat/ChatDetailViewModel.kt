package com.taskflow.app.ui.screen.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taskflow.app.domain.model.ChatMessage
import com.taskflow.app.domain.model.ChatRole
import com.taskflow.app.domain.model.Task
import com.taskflow.app.domain.repository.ChatRepository
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
    private val addTasksUseCase: AddTasksUseCase,
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatDetailUiState())
    val uiState: StateFlow<ChatDetailUiState> = _uiState.asStateFlow()

    private var currentChatId: String? = null
    private var loadJob: Job? = null

    fun loadChat(chatId: String) {
        if (currentChatId == chatId) return
        currentChatId = chatId
        loadJob?.cancel()
        // 修复：切换会话时重置状态
        _uiState.value = ChatDetailUiState()
        loadJob = viewModelScope.launch {
            getChatMessagesUseCase(chatId).collectLatest { messages ->
                _uiState.value = _uiState.value.copy(messages = messages)
            }
        }
        // 修复：加载 chat 标题
        viewModelScope.launch {
            val chat = chatRepository.getChatById(chatId)
            _uiState.value = _uiState.value.copy(chatTitle = chat?.title ?: "")
        }
    }

    fun sendMessage(content: String) {
        val chatId = currentChatId ?: run {
            _uiState.value = _uiState.value.copy(error = "会话未就绪，请稍后重试")
            return
        }
        if (content.isBlank() || _uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val result = sendChatMessageUseCase(chatId, content)
                if (result == null) {
                    _uiState.value = _uiState.value.copy(
                        error = "会话不存在或已被删除"
                    )
                }
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
        if (_uiState.value.isLoading) return

        val messages = _uiState.value.messages
        val lastUserIdx = messages.indexOfLast { it.role == ChatRole.USER }
        if (lastUserIdx == -1) return

        val userContent = messages[lastUserIdx].content
        val userMsgTimestamp = messages[lastUserIdx].timestamp

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                // 修复：删除最后一条 user 消息及之后的所有消息，再重新发送
                chatRepository.deleteMessagesFrom(chatId, userMsgTimestamp)
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

    fun createNewChat(onCreated: (String) -> Unit, onError: (String) -> Unit = {}) {
        viewModelScope.launch {
            try {
                val chat = createChatUseCase()
                onCreated(chat.id)
            } catch (e: Exception) {
                onError(e.message ?: "创建会话失败")
            }
        }
    }

    fun addTasksToTaskList(tasks: List<Task>, onResult: (Boolean, String?) -> Unit = { _, _ -> }) {
        viewModelScope.launch {
            try {
                addTasksUseCase(tasks)
                onResult(true, null)
            } catch (e: Exception) {
                onResult(false, e.message)
            }
        }
    }
}
