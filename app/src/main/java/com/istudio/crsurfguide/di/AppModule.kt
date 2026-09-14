package com.istudio.crsurfguide.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.istudio.crsurfguide.data.repository.AuthRepositoryImpl
import com.istudio.crsurfguide.data.repository.SurfRepositoryImpl
import com.istudio.crsurfguide.domain.repository.AuthRepository
import com.istudio.crsurfguide.domain.repository.SurfRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
    fun provideAuthRepository(auth: FirebaseAuth, firestore: FirebaseFirestore): AuthRepository = 
        AuthRepositoryImpl(auth, firestore)

    @Provides
    @Singleton
    fun provideSurfRepository(firestore: FirebaseFirestore): SurfRepository = 
        SurfRepositoryImpl(firestore)
}
