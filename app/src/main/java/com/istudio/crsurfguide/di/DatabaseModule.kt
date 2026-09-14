package com.istudio.crsurfguide.di

import android.content.Context
import androidx.room.Room
import com.istudio.crsurfguide.data.local.AppDatabase
import com.istudio.crsurfguide.data.local.dao.FavoriteSpotDao
import com.istudio.crsurfguide.data.local.dao.SurfSpotDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "crsurfguide_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideSurfSpotDao(db: AppDatabase): SurfSpotDao {
        return db.surfSpotDao
    }

    @Provides
    @Singleton
    fun provideFavoriteSpotDao(db: AppDatabase): FavoriteSpotDao {
        return db.favoriteSpotDao
    }
}
