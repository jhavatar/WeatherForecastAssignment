package io.chthonic.weather.data.geocoding.models

import io.chthonic.weather.common.models.GeocodingCity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class NominatimResult(
    val lat: Double,
    val lon: Double,
    @SerialName("display_name") val displayName: String,
    val address: NominatimAddress? = null,
) {
    fun toDomainModel(): GeocodingCity = GeocodingCity(
        lat,
        lon,
        address?.city?.let { city -> "$city${address.country?.let { ", $it" } ?: ""}" }
            ?: displayName
    )
}

@Serializable
internal data class NominatimAddress(
    val city: String? = null,
    val town: String? = null,
    val village: String? = null,
    val county: String? = null,
    val state: String? = null,
    val country: String? = null,
    @SerialName("country_code") val countryCode: String? = null,
)
