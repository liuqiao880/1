package com.taskflow.app.ui.screen.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taskflow.app.domain.model.ChatMessage
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
    val chatTitle: String = ""
)

@HiltViewModel
class ChatDetailViewModel @Inject constructor(
    private val getChatMessagesUseCase: GetChatMessagesUseCase,
    private val sendChatMessageUseCase: SendChatMessageUseCase,
    private val createChatUseCase: CreateChatUseCase
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
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                sendChatMessageUseCase(chatId, content)
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun createNewChat(onCreated: (String) -> Unit) {
        viewModelScope.launch {
            val chat = createChatUseCase()
            onCreated(chat.id)
        }
    }
}
