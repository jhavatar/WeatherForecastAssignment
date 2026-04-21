package io.chthonic.weather.common.models

import androidx.compose.runtime.Immutable

@Immutable
data class GeocodingCity(
    val location: Location,
    val displayName: String,
)