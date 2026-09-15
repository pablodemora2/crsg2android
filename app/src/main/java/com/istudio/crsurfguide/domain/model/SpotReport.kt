package com.istudio.crsurfguide.domain.model

data class SpotReport(
    val id: String = "",
    val spotId: String = "",
    val userId: String = "",
    val userName: String = "",
    val userProfileImageUrl: String = "",
    val imageUrl: String = "",
    val comment: String = "",
    val waveHeight: String = "",
    val windCondition: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
