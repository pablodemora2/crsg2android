package com.istudio.crsurfguide.domain.usecase

import com.istudio.crsurfguide.domain.model.ChatMessage
import com.istudio.crsurfguide.domain.repository.ChatRepository
import com.istudio.crsurfguide.domain.repository.UserRepository
import com.istudio.crsurfguide.ui.debug.LogBuffer
import javax.inject.Inject

class SeedFakeUserDataUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(uid: String): Result<Unit> {
        if (!uid.startsWith("fake_")) return Result.success(Unit)

        LogBuffer.d("SeedUseCase", "Iniciando seeding para usuario: $uid")
        
        return try {
            userRepository.getUserProfile(uid).onSuccess { profile ->
                // Solo poblar si el perfil está "vacío" o tiene valores por defecto
                if (profile.bio.isEmpty() || profile.bio.contains("Surfista apasionado")) {
                    val updatedProfile = profile.copy(
                        bio = "Amante de las izquierdas en Guanacaste. Persiguiendo el swell perfecto por toda la costa.",
                        surfLevel = "Avanzado",
                        favoriteSpot = "Pavones"
                    )
                    userRepository.updateUserProfile(updatedProfile)
                    
                    // Sembrar favoritos (IDs simulados comunes)
                    val seedFavorites = listOf("witchs_rock", "pavones", "santa_teresa")
                    seedFavorites.forEach { spotId ->
                        // Evitar duplicados si ya existen
                        if (!profile.favoriteSurfSpotIds.contains(spotId)) {
                            userRepository.toggleFavoriteSpot(uid, spotId)
                        }
                    }

                    // Sembrar mensajes iniciales en el chat general
                    val welcomeMessages = listOf(
                        "¡Pura Vida! Acabo de unirme a la comunidad.",
                        "¿Alguien sabe cómo está el reporte para mañana en Roca Bruja?",
                        "Hoy las olas en Santa Teresa estuvieron épicas 🌊"
                    )
                    
                    welcomeMessages.forEach { text ->
                        chatRepository.sendMessage(
                            ChatMessage(
                                spotId = "general",
                                senderId = uid,
                                senderName = profile.name,
                                senderAvatarUrl = profile.profileImageUrl,
                                text = text
                            )
                        )
                    }
                    
                    LogBuffer.d("SeedUseCase", "Seeding completado con éxito para $uid")
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            LogBuffer.e("SeedUseCase", "Error durante el seeding", e)
            Result.failure(e)
        }
    }
}
