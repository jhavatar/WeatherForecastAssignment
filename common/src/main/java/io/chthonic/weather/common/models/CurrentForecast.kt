package io.chthonic.weather.common.models

import java.time.LocalDate

data class CurrentForecast(
    val date: LocalDate,
    val weatherCode: Int,
    val temp: Double,
)