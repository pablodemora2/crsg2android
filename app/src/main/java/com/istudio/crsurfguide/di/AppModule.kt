package com.istudio.crsurfguide.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.istudio.crsurfguide.data.local.dao.FavoriteSpotDao
import com.istudio.crsurfguide.data.local.dao.SurfSpotDao
import com.istudio.crsurfguide.data.remote.WeatherApi
import com.istudio.crsurfguide.data.repository.AuthRepositoryImpl
import com.istudio.crsurfguide.data.repository.SurfRepositoryImpl
import com.istudio.crsurfguide.data.repository.UserRepositoryImpl
import com.istudio.crsurfguide.data.repository.WeatherRepositoryImpl
import com.istudio.crsurfguide.domain.repository.AuthRepository
import com.istudio.crsurfguide.domain.repository.SurfRepository
import com.istudio.crsurfguide.domain.repository.UserRepository
import com.istudio.crsurfguide.domain.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    @Provides
    @Singleton
    fun provideWeatherApi(): WeatherApi = Retrofit.Builder()
        .baseUrl("https://api.open-meteo.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(WeatherApi::class.java)

    @Provides
    @Singleton
    fun provideAuthRepository(auth: FirebaseAuth, firestore: FirebaseFirestore): AuthRepository = 
        AuthRepositoryImpl(auth, firestore)

    @Provides
    @Singleton
    fun provideSurfRepository(firestore: FirebaseFirestore, surfSpotDao: SurfSpotDao): SurfRepository = 
        SurfRepositoryImpl(firestore, surfSpotDao)

    @Provides
    @Singleton
    fun provideUserRepository(
        firestore: FirebaseFirestore,
        storage: FirebaseStorage,
        favoriteSpotDao: FavoriteSpotDao
    ): UserRepository = UserRepositoryImpl(firestore, storage, favoriteSpotDao)

    @Provides
    @Singleton
    fun provideWeatherRepository(api: WeatherApi): WeatherRepository = 
        WeatherRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideSurfReportRepository(
        firestore: FirebaseFirestore,
        storage: FirebaseStorage
    ): com.istudio.crsurfguide.domain.repository.SurfReportRepository = 
        com.istudio.crsurfguide.data.repository.SurfReportRepositoryImpl(firestore, storage)

    @Provides
    @Singleton
    fun provideChatRepository(
        firestore: FirebaseFirestore
    ): com.istudio.crsurfguide.domain.repository.ChatRepository = 
        com.istudio.crsurfguide.data.repository.ChatRepositoryImpl(firestore)
}
