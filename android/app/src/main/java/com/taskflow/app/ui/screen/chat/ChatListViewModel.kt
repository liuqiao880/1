package com.taskflow.app.ui.screen.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.taskflow.app.domain.model.Chat
import com.taskflow.app.domain.usecase.CreateChatUseCase
import com.taskflow.app.domain.usecase.DeleteChatUseCase
import com.taskflow.app.domain.usecase.GetAllChatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatListUiState(
    val chats: List<Chat> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val getAllChatsUseCase: GetAllChatsUseCase,
    private val createChatUseCase: CreateChatUseCase,
    private val deleteChatUseCase: DeleteChatUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatListUiState())
    val uiState: StateFlow<ChatListUiState> = _uiState.asStateFlow()

    init {
        loadChats()
    }

    private fun loadChats() {
        viewModelScope.launch {
            getAllChatsUseCase().collectLatest { chats ->
                _uiState.value = _uiState.value.copy(
                    chats = chats,
                    isLoading = false
                )
            }
        }
    }

    fun createChat(onCreated: (String) -> Unit) {
        viewModelScope.launch {
            val chat = createChatUseCase()
            onCreated(chat.id)
        }
    }

    fun deleteChat(chatId: String) {
        viewModelScope.launch {
            deleteChatUseCase(chatId)
        }
    }
}
