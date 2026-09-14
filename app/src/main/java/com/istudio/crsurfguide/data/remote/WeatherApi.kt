package com.istudio.crsurfguide.data.remote

import com.istudio.crsurfguide.data.remote.dto.MarineResponseDto
import com.istudio.crsurfguide.data.remote.dto.WeatherResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("https://api.open-meteo.com/v1/forecast")
    suspend fun getWeatherData(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("current") current: String = "temperature_2m,wind_speed_10m,wind_direction_10m"
    ): WeatherResponseDto

    @GET("https://marine-api.open-meteo.com/v1/marine")
    suspend fun getMarineData(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("current") current: String = "wave_height,wave_direction,wave_period"
    ): MarineResponseDto
}
