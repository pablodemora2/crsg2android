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
        val docRef = firestore.collection("spots")
            .document(message.spotId)
            .collection("messages")
            .add(message)
            .await()
        
        // Actualizar el ID del mensaje con el ID generado por Firestore
        docRef.update("id", docRef.id).await()
        
        Result.success(true)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun deleteMessage(spotId: String, messageId: String): Result<Boolean> = try {
        firestore.collection("spots")
            .document(spotId)
            .collection("messages")
            .document(messageId)
            .delete()
            .await()
        Result.success(true)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun clearChatHistory(spotId: String): Result<Boolean> = try {
        val messages = firestore.collection("spots")
            .document(spotId)
            .collection("messages")
            .get()
            .await()
        
        val batch = firestore.batch()
        messages.documents.forEach { batch.delete(it.reference) }
        batch.commit().await()

        Result.success(true)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
