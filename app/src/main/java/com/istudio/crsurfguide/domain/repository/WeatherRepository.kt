package com.istudio.crsurfguide.domain.repository

import com.istudio.crsurfguide.domain.model.SurfWeather

interface WeatherRepository {
    suspend fun getSurfWeather(lat: Double, lon: Double): Result<SurfWeather>
}
