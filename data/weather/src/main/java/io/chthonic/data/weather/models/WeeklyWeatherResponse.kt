package io.chthonic.data.weather.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeeklyWeatherResponse(
    @SerialName("daily") val daily: DailyWeather,
)