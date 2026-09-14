package com.istudio.crsurfguide.domain.repository

import com.istudio.crsurfguide.domain.model.SurfSpot
import kotlinx.coroutines.flow.Flow

interface SurfRepository {
    fun getSurfSpots(): Flow<Result<List<SurfSpot>>>
    fun getSpotsByZone(zone: String): Flow<Result<List<SurfSpot>>>
    suspend fun getSpotById(id: String): Result<SurfSpot>
}
