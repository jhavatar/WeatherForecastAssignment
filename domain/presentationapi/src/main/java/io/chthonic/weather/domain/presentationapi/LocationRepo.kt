package io.chthonic.weather.domain.presentationapi

import io.chthonic.weather.common.models.Location
import io.chthonic.weather.common.models.Outcome

interface LocationRepo {
    suspend fun getCurrentLocation(): Outcome<Location>
}
