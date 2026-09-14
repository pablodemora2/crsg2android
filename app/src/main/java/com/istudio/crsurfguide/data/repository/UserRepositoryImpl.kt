package com.istudio.crsurfguide.data.repository

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import com.google.firebase.storage.FirebaseStorage
import com.istudio.crsurfguide.data.local.dao.FavoriteSpotDao
import com.istudio.crsurfguide.data.local.entity.FavoriteSpotEntity
import com.istudio.crsurfguide.domain.model.UserProfile
import com.istudio.crsurfguide.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val favoriteSpotDao: FavoriteSpotDao
) : UserRepository {

    override suspend fun getUserProfile(uid: String): Result<UserProfile> = try {
        val snapshot = firestore.collection("users").document(uid).get().await()
        val profile = snapshot.toObject(UserProfile::class.java)
        if (profile != null) {
            // Sincronizar favoritos locales
            val favorites = profile.favoriteSurfSpotIds.map { FavoriteSpotEntity(it) }
            favoriteSpotDao.clearAll()
            favoriteSpotDao.insertFavorites(favorites)
            
            Result.success(profile)
        } else {
            Result.failure(Exception("Profile not found"))
        }
    } catch (e: Exception) {
        // Intentar construir perfil parcial si hay favoritos locales en caso de offline
        // Pero UserProfile tiene más datos. Por ahora retornamos error si falla red.
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
        
        firestore.collection("users").document(uid).update("profileImageUrl", url).await()
        
        Result.success(url)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun toggleFavoriteSpot(uid: String, spotId: String): Result<Boolean> = try {
        val userDoc = firestore.collection("users").document(uid)
        val snapshot = userDoc.get().await()
        val currentFavorites = snapshot.get("favoriteSurfSpotIds") as? List<String> ?: emptyList()
        
        if (currentFavorites.contains(spotId)) {
            userDoc.update("favoriteSurfSpotIds", FieldValue.arrayRemove(spotId)).await()
            favoriteSpotDao.removeFavorite(spotId)
        } else {
            userDoc.update("favoriteSurfSpotIds", FieldValue.arrayUnion(spotId)).await()
            favoriteSpotDao.insertFavorites(listOf(FavoriteSpotEntity(spotId)))
        }
        Result.success(true)
    } catch (e: Exception) {
        // En caso de fallo de red, intentar actualizar localmente al menos
        try {
            favoriteSpotDao.insertFavorites(listOf(FavoriteSpotEntity(spotId)))
            Result.success(true)
        } catch (localError: Exception) {
            Result.failure(e)
        }
    }

    override fun getLocalFavoriteIds(): Flow<List<String>> = 
        favoriteSpotDao.getAllFavorites().map { entities -> entities.map { it.spotId } }
}
