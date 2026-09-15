package com.istudio.crsurfguide.data.repository

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import com.google.firebase.storage.FirebaseStorage
import com.istudio.crsurfguide.data.local.dao.FavoriteSpotDao
import com.istudio.crsurfguide.data.local.dao.UserDao
import com.istudio.crsurfguide.data.local.entity.FavoriteSpotEntity
import com.istudio.crsurfguide.data.local.entity.UserEntity
import com.istudio.crsurfguide.domain.model.UserProfile
import com.istudio.crsurfguide.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val favoriteSpotDao: FavoriteSpotDao,
    private val userDao: UserDao
) : UserRepository {

    override suspend fun getUserProfile(uid: String): Result<UserProfile> = try {
        // Primero ver si es un Fake User almacenado localmente
        val localUser = userDao.getUserById(uid)
        if (localUser != null) {
            Result.success(localUser.toUserProfile())
        } else {
            val snapshot = firestore.collection("users").document(uid).get().await()
            val profile = snapshot.toObject(UserProfile::class.java)
            if (profile != null) {
                val favorites = profile.favoriteSurfSpotIds.map { FavoriteSpotEntity(it) }
                favoriteSpotDao.clearAll()
                favoriteSpotDao.insertFavorites(favorites)
                Result.success(profile)
            } else {
                Result.failure(Exception("Profile not found"))
            }
        }
    } catch (e: Exception) {
        val localUser = userDao.getUserById(uid)
        if (localUser != null) Result.success(localUser.toUserProfile())
        else Result.failure(e)
    }

    override suspend fun updateUserProfile(user: UserProfile): Result<Boolean> = try {
        val localUser = userDao.getUserById(user.uid)
        if (localUser != null) {
            userDao.insertUser(UserEntity(user.uid, user.name, user.email, user.profileImageUrl, user.bio, user.surfLevel, user.favoriteSpot))
            Result.success(true)
        } else {
            firestore.collection("users").document(user.uid).set(user).await()
            Result.success(true)
        }
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
            com.google.firebase.messaging.FirebaseMessaging.getInstance().unsubscribeFromTopic("topic_spot_$spotId")
        } else {
            userDoc.update("favoriteSurfSpotIds", FieldValue.arrayUnion(spotId)).await()
            favoriteSpotDao.insertFavorites(listOf(FavoriteSpotEntity(spotId)))
            com.google.firebase.messaging.FirebaseMessaging.getInstance().subscribeToTopic("topic_spot_$spotId")
        }
        Result.success(true)
    } catch (e: Exception) {
        try {
            favoriteSpotDao.insertFavorites(listOf(FavoriteSpotEntity(spotId)))
            Result.success(true)
        } catch (localError: Exception) {
            Result.failure(e)
        }
    }

    override fun getLocalFavoriteIds(): Flow<List<String>> = 
        favoriteSpotDao.getAllFavorites().map { entities -> entities.map { it.spotId } }

    override suspend fun getOrCreateFakeUser(name: String, email: String, avatarUrl: String): UserProfile {
        val searchName = if (name == "fake_qa_default") "QA Tester" else name
        val existing = userDao.getUserByName(searchName)
        if (existing != null) {
            return existing.toUserProfile()
        }
        val newUid = if (name == "fake_qa_default") "fake_qa_default" else "fake_" + UUID.randomUUID().toString()
        val newEntity = UserEntity(
            uid = newUid,
            name = searchName,
            email = email.ifEmpty { "qa@example.com" },
            profileImageUrl = avatarUrl.ifEmpty { "https://ui-avatars.com/api/?name=QA+Tester" },
            bio = "Surfista apasionado testeando la app.",
            surfLevel = "Principiante",
            favoriteSpot = "Playa Jacó"
        )
        userDao.insertUser(newEntity)
        return newEntity.toUserProfile()
    }
}
