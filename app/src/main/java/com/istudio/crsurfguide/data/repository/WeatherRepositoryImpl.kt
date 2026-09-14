package com.istudio.crsurfguide.data.repository

import com.istudio.crsurfguide.data.remote.WeatherApi
import com.istudio.crsurfguide.domain.model.SurfWeather
import com.istudio.crsurfguide.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi
) : WeatherRepository {
    override suspend fun getSurfWeather(lat: Double, lon: Double): Result<SurfWeather> = try {
        val weatherDto = api.getWeatherData(lat, lon)
        val marineDto = api.getMarineData(lat, lon)
        
        val surfWeather = SurfWeather(
            temperature = weatherDto.current?.temperature ?: 0.0,
            windSpeed = weatherDto.current?.windSpeed ?: 0.0,
            windDirection = weatherDto.current?.windDirection ?: 0,
            waveHeight = marineDto.current?.waveHeight ?: 0.0,
            waveDirection = marineDto.current?.waveDirection ?: 0,
            wavePeriod = marineDto.current?.wavePeriod ?: 0.0
        )
        Result.success(surfWeather)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
