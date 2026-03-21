package io.chthonic.weather.data.geocoding

import io.chthonic.weather.common.models.GeocodingCity
import io.chthonic.weather.common.models.Outcome
import io.chthonic.weather.data.common.rest.executeAsResult
import io.chthonic.weather.data.geocoding.models.NominatimResult
import io.chthonic.weather.domain.dataapi.GeocodingService
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject

private const val NOMINATIM_CITY_END_POINT = "https://nominatim.openstreetmap.org/search"
private const val CITY_LIMIT = 20

internal class GeocodingServiceImpl @Inject constructor(
    private val client: HttpClient,
) : GeocodingService {

    override suspend fun searchCity(query: String): Outcome<List<GeocodingCity>> {
        val results: Outcome<List<NominatimResult>> = client.executeAsResult {
            client.get(NOMINATIM_CITY_END_POINT) {
                parameter("q", query)
                parameter("format", "json")
                parameter("limit", CITY_LIMIT)
            }
        }

        return results.map { data ->
            data.map {
                it.toDomainModel()
            }
        }
    }
}