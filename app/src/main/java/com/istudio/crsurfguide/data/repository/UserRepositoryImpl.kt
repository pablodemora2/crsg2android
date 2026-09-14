package com.istudio.crsurfguide.data.repository

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.istudio.crsurfguide.domain.model.UserProfile
import com.istudio.crsurfguide.domain.repository.UserRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
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

    override suspend fun uploadProfileImage(uid: String, imageUri: Uri): Result<String> = try {
        val ref = storage.reference.child("profiles/$uid.jpg")
        ref.putFile(imageUri).await()
        val url = ref.downloadUrl.await().toString()
        
        // Actualizar Firestore con la nueva URL
        firestore.collection("users").document(uid).update("profileImageUrl", url).await()
        
        Result.success(url)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
