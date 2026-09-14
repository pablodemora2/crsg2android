package com.istudio.crsurfguide.data.local.dao

import androidx.room.*
import com.istudio.crsurfguide.data.local.entity.FavoriteSpotEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteSpotDao {
    @Query("SELECT * FROM favorite_spots")
    fun getAllFavorites(): Flow<List<FavoriteSpotEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorites(favorites: List<FavoriteSpotEntity>)

    @Query("DELETE FROM favorite_spots WHERE spotId = :spotId")
    suspend fun removeFavorite(spotId: String)

    @Query("DELETE FROM favorite_spots")
    suspend fun clearAll()
}
