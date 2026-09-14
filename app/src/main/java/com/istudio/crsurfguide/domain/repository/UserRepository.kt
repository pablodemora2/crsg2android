package com.istudio.crsurfguide.domain.repository

import com.istudio.crsurfguide.domain.model.UserProfile

interface UserRepository {
    suspend fun getUserProfile(uid: String): Result<UserProfile>
    suspend fun updateUserProfile(user: UserProfile): Result<Boolean>
}
