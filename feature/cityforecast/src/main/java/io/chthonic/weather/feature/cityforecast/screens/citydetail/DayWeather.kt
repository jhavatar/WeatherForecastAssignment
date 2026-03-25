package io.chthonic.weather.feature.cityforecast.screens.citydetail

import androidx.compose.ui.graphics.vector.ImageVector
import io.chthonic.weather.common.models.DayForecast
import io.chthonic.weather.feature.cityforecast.models.WeatherCondition
import io.chthonic.weather.feature.cityforecast.models.toIcon
import io.chthonic.weather.feature.cityforecast.models.toWeatherCondition
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

internal class DayWeather(
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

internal fun DayForecast.toDayWeather(): DayWeather = DayWeather(
    date = date,
    weatherCondition = weatherCode.toWeatherCondition(),
    minTemp = minTemp,
    maxTemp = maxTemp,
)