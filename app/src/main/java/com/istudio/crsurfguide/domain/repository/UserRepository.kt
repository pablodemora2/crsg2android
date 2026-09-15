package com.istudio.crsurfguide.domain.repository

import android.net.Uri
import com.istudio.crsurfguide.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUserProfile(uid: String): Result<UserProfile>
    suspend fun updateUserProfile(user: UserProfile): Result<Boolean>
    suspend fun uploadProfileImage(uid: String, imageUri: Uri): Result<String>
    suspend fun toggleFavoriteSpot(uid: String, spotId: String): Result<Boolean>
    fun getLocalFavoriteIds(): Flow<List<String>>
    suspend fun getOrCreateFakeUser(name: String, email: String, avatarUrl: String): UserProfile
}
