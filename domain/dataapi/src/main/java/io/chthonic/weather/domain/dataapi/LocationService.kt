package io.chthonic.weather.domain.dataapi

import io.chthonic.weather.common.models.Location
import io.chthonic.weather.common.models.Outcome
import kotlinx.coroutines.flow.Flow

interface LocationService {

    /**
     * Request location updates.
     * @param minUpdateIntervalMs Minimum time between location updates in milliseconds.
     * @param minUpdateDistanceM Minimum distance between location updates in meters.
     */
    fun getLocationUpdates(
        minUpdateIntervalMs: Long = 10_000L,
        minUpdateDistanceM: Float = 50f,
    ): Flow<Outcome<Location>>
}
