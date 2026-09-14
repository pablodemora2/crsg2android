package com.istudio.crsurfguide.domain.repository

import android.net.Uri
import com.istudio.crsurfguide.domain.model.UserProfile

interface UserRepository {
    suspend fun getUserProfile(uid: String): Result<UserProfile>
    suspend fun updateUserProfile(user: UserProfile): Result<Boolean>
    suspend fun uploadProfileImage(uid: String, imageUri: Uri): Result<String>
}
