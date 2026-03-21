package io.chthonic.data.weather.models

import io.chthonic.weather.common.models.CurrentForecast
import io.chthonic.weather.common.models.DayForecast
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class CurrentWeather(
    @SerialName("temperature_2m") val temperatureCelsius: Double,
    @SerialName("relative_humidity_2m") val humidityPercent: Int,
    @SerialName("weather_code") val weatherCode: Int,
    @SerialName("wind_speed_10m") val windSpeed: Double,
    @SerialName("is_day") val isDay: Int,
) {
    fun toDomainModel(): CurrentForecast {
        return CurrentForecast(
            date = LocalDate.now(),
            weatherCode = weatherCode,
            temp = temperatureCelsius,
        )
    }
}