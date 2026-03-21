package io.chthonic.weather.data.geocoding.models

import io.chthonic.weather.common.models.GeocodingCity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NominatimResult(
    val lat: Double,
    val lon: Double,
    @SerialName("display_name") val displayName: String,
) {
    fun toDomainModel(): GeocodingCity = GeocodingCity(lat, lon, displayName)
}
