package io.chthonic.weather.presentation.screens.location

import androidx.compose.ui.graphics.vector.ImageVector
import io.chthonic.weather.common.models.DayForecast
import io.chthonic.weather.presentation.models.WeatherCondition
import io.chthonic.weather.presentation.models.toIcon
import io.chthonic.weather.presentation.models.toWeatherCondition
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

class DayWeather(
    val date: LocalDate,
    val weatherCondition: WeatherCondition,
    val minTemp: Double,
    val maxTemp: Double,
) {
    val key: Long by lazy {
        date.toEpochDay()
    }

    val dayName: String by lazy {
        when (date) {
            LocalDate.now() -> "Today"
            LocalDate.now().plusDays(1) -> "Tomorrow"
            else -> date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
        }
    }
    
    val weatherConditionIcon: ImageVector by lazy {
        weatherCondition.toIcon()
    }
}

fun DayForecast.toDayWeather(): DayWeather = DayWeather(
    date = date,
    weatherCondition = weatherCode.toWeatherCondition(),
    minTemp = minTemp,
    maxTemp = maxTemp,
)