package io.chthonic.weather.domain.presentationapi

import io.chthonic.weather.common.models.Location
import io.chthonic.weather.common.models.Outcome
import kotlinx.coroutines.flow.Flow

interface LocationRepo {
    fun getLocationUpdates(): Flow<Outcome<Location>>
}
