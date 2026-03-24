package io.chthonic.weather.presentation.screens.locationlist

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.HourglassEmpty
import androidx.compose.ui.graphics.vector.ImageVector
import io.chthonic.weather.common.models.GeocodingCity
import io.chthonic.weather.common.models.Location
import io.chthonic.weather.presentation.models.WeatherCondition
import io.chthonic.weather.presentation.models.toIcon

data class LocationCurrentWeather(
    val location: Location,
    val displayName: String,
    val weatherCondition: WeatherCondition? = null,
    val temp: Double? = null,
    val weatherError: Boolean = false,
) {

    val key: Int by lazy {
        location.hashCode()
    }

    val hasTemp: Boolean
        get() = temp != null

    val isLoading: Boolean
        get() = !hasTemp && !weatherError

    val displayCoords: String by lazy {
        location.format()
    }

    private val weatherConditionIcon: ImageVector?
        get() = weatherCondition?.toIcon()

    val displayIcon: ImageVector
        get() = (if (weatherError) Icons.Outlined.ErrorOutline else weatherConditionIcon)
            ?: Icons.Outlined.HourglassEmpty
}

fun GeocodingCity.toLocationCurrentWeather(): LocationCurrentWeather = LocationCurrentWeather(
    location = Location(lat = lat, lon = lon),
    displayName = displayName,
)