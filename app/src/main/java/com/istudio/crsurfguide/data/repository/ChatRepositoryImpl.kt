package com.istudio.crsurfguide.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.istudio.crsurfguide.domain.model.ChatMessage
import com.istudio.crsurfguide.domain.repository.ChatRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : ChatRepository {

    override fun getMessages(spotId: String): Flow<List<ChatMessage>> = callbackFlow {
        val registration = firestore.collection("spots")
            .document(spotId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    com.istudio.crsurfguide.ui.debug.LogBuffer.e("ChatRepository", "Error en SnapshotListener para spot: $spotId", error)
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val messages = snapshot.toObjects(ChatMessage::class.java)
                    com.istudio.crsurfguide.ui.debug.LogBuffer.d("ChatRepository", "Recibidos ${messages.size} mensajes de Firestore para spot: $spotId")
                    trySend(messages)
                }
            }
        awaitClose { registration.remove() }
    }

    override suspend fun sendMessage(message: ChatMessage): Result<Boolean> = try {
        firestore.collection("spots")
            .document(message.spotId)
            .collection("messages")
            .add(message)
            .await()
        Result.success(true)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
