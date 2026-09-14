package com.istudio.crsurfguide.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.istudio.crsurfguide.domain.model.UserProfile
import com.istudio.crsurfguide.domain.repository.UserRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserRepository {

    override suspend fun getUserProfile(uid: String): Result<UserProfile> = try {
        val snapshot = firestore.collection("users").document(uid).get().await()
        val profile = snapshot.toObject(UserProfile::class.java)
        if (profile != null) {
            Result.success(profile)
        } else {
            Result.failure(Exception("Profile not found"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun updateUserProfile(user: UserProfile): Result<Boolean> = try {
        firestore.collection("users").document(user.uid).set(user).await()
        Result.success(true)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
