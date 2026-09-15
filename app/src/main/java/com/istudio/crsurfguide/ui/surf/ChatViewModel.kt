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

    private var messagesJob: kotlinx.coroutines.Job? = null

    fun getCurrentUserId(): String? {
        return SurfViewModel.forcedFakeUid ?: authRepository.getCurrentUserId()
    }

    fun loadMessages(spotId: String) {
        messagesJob?.cancel()
        messagesJob = viewModelScope.launch {
            com.istudio.crsurfguide.ui.debug.LogBuffer.d("ChatViewModel", "Cargando mensajes para spot: $spotId")
            chatRepository.getMessages(spotId).collect {
                _messages.value = it
                com.istudio.crsurfguide.ui.debug.LogBuffer.d("ChatViewModel", "Actualizados ${it.size} mensajes en el StateFlow")
            }
        }
    }

    fun sendMessage(spotId: String, text: String) {
        val uid = getCurrentUserId()
        if (text.isBlank() || uid == null) {
            com.istudio.crsurfguide.ui.debug.LogBuffer.e("ChatViewModel", "Fallo al enviar: texto vacío o UID nulo (uid=$uid)")
            return
        }

        viewModelScope.launch {
            userRepository.getUserProfile(uid).onSuccess { profile ->
                val message = ChatMessage(
                    spotId = spotId,
                    senderId = profile.uid,
                    senderName = profile.name,
                    senderAvatarUrl = profile.profileImageUrl,
                    text = text
                )
                com.istudio.crsurfguide.ui.debug.LogBuffer.d("ChatViewModel", "Enviando mensaje de ${profile.name}: ${text.take(15)}...")
                chatRepository.sendMessage(message)
            }.onFailure {
                com.istudio.crsurfguide.ui.debug.LogBuffer.e("ChatViewModel", "Error obteniendo perfil para chat", it)
            }
        }
    }
}
