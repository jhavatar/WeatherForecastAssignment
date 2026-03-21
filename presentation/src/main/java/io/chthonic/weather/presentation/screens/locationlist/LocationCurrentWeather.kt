package io.chthonic.weather.presentation.screens.locationlist

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

    val hasTemp: Boolean
        get() = temp != null

    val displayCoords: String by lazy {
        location.format()
    }

    val weatherConditionIcon: ImageVector?
        get() = weatherCondition?.toIcon()
}

fun GeocodingCity.toLocationCurrentWeather(): LocationCurrentWeather = LocationCurrentWeather(
    location = Location(lat = lat, lon = lon),
    displayName = displayName,
)