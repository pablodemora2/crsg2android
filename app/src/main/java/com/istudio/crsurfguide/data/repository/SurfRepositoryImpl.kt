package com.istudio.crsurfguide.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.istudio.crsurfguide.data.local.dao.SurfSpotDao
import com.istudio.crsurfguide.data.local.entity.toEntity
import com.istudio.crsurfguide.domain.model.SurfSpot
import com.istudio.crsurfguide.domain.repository.SurfRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SurfRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val surfSpotDao: SurfSpotDao
) : SurfRepository {

    private val repositoryScope = CoroutineScope(Dispatchers.IO)

    override fun getSurfSpots(): Flow<Result<List<SurfSpot>>> = channelFlow {
        // 1. Iniciar sincronización en segundo plano
        repositoryScope.launch {
            try {
                val snapshot = firestore.collection("surf_spots").get().await()
                val remoteSpots = snapshot.toObjects(SurfSpot::class.java)
                surfSpotDao.insertSpots(remoteSpots.map { it.toEntity() })
            } catch (e: Exception) {
                // Error de red, Room seguirá sirviendo lo que tenga
            }
        }

        // 2. Escuchar cambios en Room (Single Source of Truth)
        surfSpotDao.getAllSpots()
            .map { entities -> Result.success(entities.map { it.toSurfSpot() }) }
            .collect { send(it) }
    }

    override fun getSpotsByZone(zone: String): Flow<Result<List<SurfSpot>>> = channelFlow {
        surfSpotDao.getAllSpots()
            .map { entities -> 
                val filtered = entities.map { it.toSurfSpot() }.filter { it.zone == zone }
                Result.success(filtered) 
            }
            .collect { send(it) }
    }

    override suspend fun getSpotById(id: String): Result<SurfSpot> = try {
        val localSpot = surfSpotDao.getSpotById(id)?.toSurfSpot()
        if (localSpot != null) {
            repositoryScope.launch { refreshSpot(id) }
            Result.success(localSpot)
        } else {
            refreshSpot(id)
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    private suspend fun refreshSpot(id: String): Result<SurfSpot> = try {
        val snapshot = firestore.collection("surf_spots").document(id).get().await()
        val spot = snapshot.toObject(SurfSpot::class.java)
        if (spot != null) {
            surfSpotDao.insertSpots(listOf(spot.toEntity()))
            Result.success(spot)
        } else {
            Result.failure(Exception("Spot not found"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}
