package com.istudio.crsurfguide.domain.repository

import android.net.Uri
import com.istudio.crsurfguide.domain.model.SpotReport
import kotlinx.coroutines.flow.Flow

interface SurfReportRepository {
    suspend fun uploadReport(report: SpotReport, imageUri: Uri): Result<Boolean>
    fun getReportsForSpot(spotId: String): Flow<List<SpotReport>>
}
