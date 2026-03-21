package io.chthonic.weather.domain.presentationapi

import io.chthonic.weather.common.models.GeocodingCity
import io.chthonic.weather.common.models.Outcome

interface GeocodingRepo {
    suspend fun searchCity(query: String): Outcome<List<GeocodingCity>>
}