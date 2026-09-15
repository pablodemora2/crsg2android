package com.istudio.crsurfguide.data.repository

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.istudio.crsurfguide.domain.model.SpotReport
import com.istudio.crsurfguide.domain.repository.SurfReportRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SurfReportRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : SurfReportRepository {

    override suspend fun uploadReport(report: SpotReport, imageUri: Uri): Result<Boolean> = try {
        val timestamp = System.currentTimeMillis()
        val storageRef = storage.reference.child("spots/${report.spotId}/$timestamp.jpg")
        
        // Upload image
        storageRef.putFile(imageUri).await()
        val downloadUrl = storageRef.downloadUrl.await().toString()
        
        // Save report document
        val finalReport = report.copy(imageUrl = downloadUrl, timestamp = timestamp)
        firestore.collection("spot_reports").add(finalReport).await()
        
        Result.success(true)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override fun getReportsForSpot(spotId: String): Flow<List<SpotReport>> = callbackFlow {
        val registration = firestore.collection("spot_reports")
            .whereEqualTo("spotId", spotId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val reports = snapshot.toObjects(SpotReport::class.java)
                    trySend(reports)
                }
            }
        awaitClose { registration.remove() }
    }
}
