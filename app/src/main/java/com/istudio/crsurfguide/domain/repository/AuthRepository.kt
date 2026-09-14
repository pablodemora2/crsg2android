package com.istudio.crsurfguide.domain.repository

interface AuthRepository {
    suspend fun signInWithEmail(email: String, pass: String): Result<String>
    suspend fun signUpWithEmail(email: String, pass: String): Result<String>
    fun signOut()
    fun getCurrentUserId(): String?
}
