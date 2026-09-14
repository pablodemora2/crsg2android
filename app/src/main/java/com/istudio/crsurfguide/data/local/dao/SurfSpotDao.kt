package com.istudio.crsurfguide.data.local.dao

import androidx.room.*
import com.istudio.crsurfguide.data.local.entity.SurfSpotEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SurfSpotDao {
    @Query("SELECT * FROM surf_spots")
    fun getAllSpots(): Flow<List<SurfSpotEntity>>

    @Query("SELECT * FROM surf_spots WHERE id = :id")
    suspend fun getSpotById(id: String): SurfSpotEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpots(spots: List<SurfSpotEntity>)

    @Query("DELETE FROM surf_spots")
    suspend fun clearAll()
}
