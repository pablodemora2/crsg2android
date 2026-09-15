package com.istudio.crsurfguide.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.istudio.crsurfguide.data.local.dao.FavoriteSpotDao
import com.istudio.crsurfguide.data.local.dao.SurfSpotDao
import com.istudio.crsurfguide.data.local.dao.UserDao
import com.istudio.crsurfguide.data.local.entity.FavoriteSpotEntity
import com.istudio.crsurfguide.data.local.entity.SurfSpotEntity
import com.istudio.crsurfguide.data.local.entity.UserEntity

@Database(
    entities = [SurfSpotEntity::class, FavoriteSpotEntity::class, UserEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract val surfSpotDao: SurfSpotDao
    abstract val favoriteSpotDao: FavoriteSpotDao
    abstract val userDao: UserDao
}
