package com.istudio.crsurfguide.domain.model

data class SurfSpot(
    val id: String = "",
    val name: String = "",
    val zone: String = "",
    val description: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val imageUrl: String = "",
    val rating: Float = 0f,
    val bestTide: String = "",
    val difficulty: String = ""
)
