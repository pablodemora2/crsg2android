package com.istudio.crsurfguide.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_spots")
data class FavoriteSpotEntity(
    @PrimaryKey val spotId: String
)
