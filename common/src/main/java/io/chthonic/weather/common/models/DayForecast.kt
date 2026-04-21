package io.chthonic.weather.common.models

import androidx.compose.runtime.Immutable
import java.time.LocalDate

@Immutable
data class DayForecast(
    val date: LocalDate,
    val weatherCode: Int,
    val maxTemp: Double,
    val minTemp: Double,
)