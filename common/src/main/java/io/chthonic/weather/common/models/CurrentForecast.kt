package io.chthonic.weather.common.models

import androidx.compose.runtime.Immutable
import java.time.LocalDate

@Immutable
data class CurrentForecast(
    val date: LocalDate,
    val weatherCode: Int,
    val temp: Double,
)