package com.istudio.crsurfguide.domain.model

data class SurfWeather(
    val temperature: Double = 0.0,
    val windSpeed: Double = 0.0,
    val windDirection: Int = 0,
    val waveHeight: Double = 0.0,
    val waveDirection: Int = 0,
    val wavePeriod: Double = 0.0
)
