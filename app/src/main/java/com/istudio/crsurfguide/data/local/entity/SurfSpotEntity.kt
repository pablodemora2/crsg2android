package com.istudio.crsurfguide.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.istudio.crsurfguide.domain.model.SurfSpot

@Entity(tableName = "surf_spots")
data class SurfSpotEntity(
    @PrimaryKey val id: String,
    val name: String,
    val zone: String,
    val description: String,
    val detailedDescription: String,
    val latitude: Double,
    val longitude: Double,
    val imageUrl: String,
    val rating: Float,
    val bestTide: String,
    val tideHeight: String,
    val difficulty: String,
    val waveType: String,
    val windDirection: String
) {
    fun toSurfSpot(): SurfSpot = SurfSpot(
        id = id,
        name = name,
        zone = zone,
        description = description,
        detailedDescription = detailedDescription,
        latitude = latitude,
        longitude = longitude,
        imageUrl = imageUrl,
        rating = rating,
        bestTide = bestTide,
        tideHeight = tideHeight,
        difficulty = difficulty,
        waveType = waveType,
        windDirection = windDirection
    )
}

fun SurfSpot.toEntity(): SurfSpotEntity = SurfSpotEntity(
    id = id,
    name = name,
    zone = zone,
    description = description,
    detailedDescription = detailedDescription,
    latitude = latitude,
    longitude = longitude,
    imageUrl = imageUrl,
    rating = rating,
    bestTide = bestTide,
    tideHeight = tideHeight,
    difficulty = difficulty,
    waveType = waveType,
    windDirection = windDirection
)
