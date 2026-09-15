package com.istudio.crsurfguide.domain.model

data class ChatMessage(
    val id: String = "",
    val spotId: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val senderAvatarUrl: String = "",
    val text: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
