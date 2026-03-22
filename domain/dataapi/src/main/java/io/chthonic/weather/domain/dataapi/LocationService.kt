package io.chthonic.weather.domain.dataapi

import io.chthonic.weather.common.models.Location
import io.chthonic.weather.common.models.Outcome

interface LocationService {
    suspend fun getCurrentLocation(): Outcome<Location>
}
