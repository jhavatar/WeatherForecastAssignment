package io.chthonic.weather.common.models

import java.time.LocalDate

data class DayForecast(
    val date: LocalDate,
    val weatherCode: Int,
    val maxTemp: Double,
    val minTemp: Double,
    val precipitationProbability: Int,
)