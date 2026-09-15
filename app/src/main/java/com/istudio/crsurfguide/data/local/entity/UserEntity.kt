package com.istudio.crsurfguide.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.istudio.crsurfguide.domain.model.UserProfile

@Entity(tableName = "users_cache")
data class UserEntity(
    @PrimaryKey val uid: String,
    val name: String,
    val email: String,
    val profileImageUrl: String,
    val bio: String,
    val surfLevel: String,
    val favoriteSpot: String
) {
    fun toUserProfile(favoriteSpotIds: List<String> = emptyList()): UserProfile = UserProfile(
        uid = uid,
        name = name,
        email = email,
        profileImageUrl = profileImageUrl,
        bio = bio,
        surfLevel = surfLevel,
        favoriteSpot = favoriteSpot,
        favoriteSurfSpotIds = favoriteSpotIds
    )
}

fun UserProfile.toEntity(): UserEntity = UserEntity(
    uid = uid,
    name = name,
    email = email,
    profileImageUrl = profileImageUrl,
    bio = bio,
    surfLevel = surfLevel,
    favoriteSpot = favoriteSpot
)
