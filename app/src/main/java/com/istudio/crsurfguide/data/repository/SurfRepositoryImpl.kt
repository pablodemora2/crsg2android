package com.istudio.crsurfguide.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.istudio.crsurfguide.domain.model.SurfSpot
import com.istudio.crsurfguide.domain.repository.SurfRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SurfRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : SurfRepository {

    override fun getSurfSpots(): Flow<Result<List<SurfSpot>>> = callbackFlow {
        val subscription = firestore.collection("surf_spots")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.failure(error))
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val spots = snapshot.toObjects(SurfSpot::class.java)
                    trySend(Result.success(spots))
                }
            }
        awaitClose { subscription.remove() }
    }

    override fun getSpotsByZone(zone: String): Flow<Result<List<SurfSpot>>> = callbackFlow {
        val subscription = firestore.collection("surf_spots")
            .whereEqualTo("zone", zone)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.failure(error))
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val spots = snapshot.toObjects(SurfSpot::class.java)
                    trySend(Result.success(spots))
                }
            }
        awaitClose { subscription.remove() }
    }

    override suspend fun getSpotById(id: String): Result<SurfSpot> = try {
        val snapshot = firestore.collection("surf_spots").document(id).get().await()
        val spot = snapshot.toObject(SurfSpot::class.java)
        if (spot != null) Result.success(spot)
        else Result.failure(Exception("Spot not found"))
    } catch (e: Exception) {
        Result.failure(e)
    }
}
