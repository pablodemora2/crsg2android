package com.istudio.crsurfguide.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.istudio.crsurfguide.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override suspend fun signInWithEmail(email: String, pass: String): Result<String> = try {
        val result = auth.signInWithEmailAndPassword(email, pass).await()
        val userId = result.user?.uid ?: throw Exception("User ID is null")
        Result.success(userId)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun signUpWithEmail(email: String, pass: String): Result<String> = try {
        val result = auth.createUserWithEmailAndPassword(email, pass).await()
        val userId = result.user?.uid ?: throw Exception("User ID is null")
        
        // Create user document in Firestore
        val userData = mapOf(
            "email" to email,
            "createdAt" to System.currentTimeMillis()
        )
        firestore.collection("users").document(userId).set(userData).await()
        
        Result.success(userId)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override fun signOut() {
        auth.signOut()
    }

    override fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }
}
