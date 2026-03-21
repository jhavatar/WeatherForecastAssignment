package io.chthonic.data.weather.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeatherResponse(
    @SerialName("current") val current: CurrentWeather,
)