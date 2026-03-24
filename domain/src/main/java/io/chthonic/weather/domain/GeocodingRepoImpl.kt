package io.chthonic.weather.domain

import io.chthonic.weather.common.models.GeocodingCity
import io.chthonic.weather.common.models.Outcome
import io.chthonic.weather.domain.dataapi.GeocodingService
import io.chthonic.weather.domain.presentationapi.GeocodingRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeocodingRepoImpl @Inject constructor(
    private val geocodingService: GeocodingService,
) : GeocodingRepo {
    override suspend fun searchCity(query: String): Outcome<List<GeocodingCity>> {
        return geocodingService.searchCity(query).map { data ->
            // remove possible duplicate locations
            data.distinctBy { it.location.key }
        }
    }
}