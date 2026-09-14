package com.istudio.crsurfguide.domain.model

data class UserProfile(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val profileImageUrl: String = "",
    val surfLevel: String = "",
    val favoriteSpot: String = "",
    val favoriteSurfSpotIds: List<String> = emptyList()
)
