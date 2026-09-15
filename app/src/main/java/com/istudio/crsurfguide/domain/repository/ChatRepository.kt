package com.istudio.crsurfguide.domain.repository

import com.istudio.crsurfguide.domain.model.ChatMessage
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getMessages(spotId: String): Flow<List<ChatMessage>>
    suspend fun sendMessage(message: ChatMessage): Result<Boolean>
    suspend fun deleteMessage(spotId: String, messageId: String): Result<Boolean>
    suspend fun clearChatHistory(spotId: String): Result<Boolean>
}
