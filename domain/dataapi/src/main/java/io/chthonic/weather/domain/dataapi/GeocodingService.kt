package io.chthonic.weather.domain.dataapi

import io.chthonic.weather.common.models.Outcome
import io.chthonic.weather.common.models.GeocodingCity

interface GeocodingService {
    suspend fun searchCity(query: String): Outcome<List<GeocodingCity>>
}