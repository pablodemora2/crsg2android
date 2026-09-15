package com.istudio.crsurfguide.ui.surf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.istudio.crsurfguide.domain.model.ChatMessage
import com.istudio.crsurfguide.domain.repository.AuthRepository
import com.istudio.crsurfguide.domain.repository.ChatRepository
import com.istudio.crsurfguide.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _currentUserId = authRepository.getCurrentUserId()
    val currentUserId: String? = _currentUserId

    fun loadMessages(spotId: String) {
        viewModelScope.launch {
            chatRepository.getMessages(spotId).collect {
                _messages.value = it
            }
        }
    }

    fun sendMessage(spotId: String, text: String) {
        if (text.isBlank() || _currentUserId == null) return

        viewModelScope.launch {
            userRepository.getUserProfile(_currentUserId).onSuccess { profile ->
                val message = ChatMessage(
                    spotId = spotId,
                    senderId = profile.uid,
                    senderName = profile.name,
                    senderAvatarUrl = profile.profileImageUrl,
                    text = text
                )
                chatRepository.sendMessage(message)
            }
        }
    }
}
