package com.istudio.crsurfguide.data.remote.dto

import com.google.gson.annotations.SerializedName

data class WeatherResponseDto(
    @SerializedName("current") val current: CurrentWeatherDto?
)

data class CurrentWeatherDto(
    @SerializedName("temperature_2m") val temperature: Double,
    @SerializedName("wind_speed_10m") val windSpeed: Double,
    @SerializedName("wind_direction_10m") val windDirection: Int
)

data class MarineResponseDto(
    @SerializedName("current") val current: CurrentMarineDto?
)

data class CurrentMarineDto(
    @SerializedName("wave_height") val waveHeight: Double,
    @SerializedName("wave_direction") val waveDirection: Int,
    @SerializedName("wave_period") val wavePeriod: Double
)
