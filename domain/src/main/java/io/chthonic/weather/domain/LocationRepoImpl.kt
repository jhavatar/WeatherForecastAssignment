package io.chthonic.weather.domain

import io.chthonic.weather.common.models.Location
import io.chthonic.weather.common.models.Outcome
import io.chthonic.weather.domain.dataapi.LocationService
import io.chthonic.weather.domain.presentationapi.LocationRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepoImpl @Inject constructor(
    private val locationService: LocationService,
) : LocationRepo {
    override fun getLocationUpdates(): Flow<Outcome<Location>> {
        return locationService.getLocationUpdates()
    }
}
