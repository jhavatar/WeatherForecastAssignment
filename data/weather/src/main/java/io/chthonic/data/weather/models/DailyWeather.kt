package io.chthonic.data.weather.models

import io.chthonic.weather.common.models.DayForecast
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class DailyWeather(
    @SerialName("time") val dates: List<String>,
    @SerialName("weather_code") val weatherCodes: List<Int>,
    @SerialName("temperature_2m_max") val maxTemperatures: List<Double>,
    @SerialName("temperature_2m_min") val minTemperatures: List<Double>,
) {
    fun toDomainModel(): List<DayForecast> {
        return buildList {
            for (idx in 0..7) {
                val date = dates.getOrNull(idx) ?: break
                val weatherCode = weatherCodes.getOrNull(idx) ?: break
                val minTemp = minTemperatures.getOrNull(idx) ?: break
                val maxTemp = maxTemperatures.getOrNull(idx) ?: break
                add(
                    DayForecast(
                        date = LocalDate.parse(date),
                        weatherCode = weatherCode,
                        maxTemp = maxTemp,
                        minTemp = minTemp,
                    )
                )
            }
        }
    }
}