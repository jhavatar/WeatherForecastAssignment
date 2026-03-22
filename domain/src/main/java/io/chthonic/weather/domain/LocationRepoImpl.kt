package io.chthonic.weather.domain

import io.chthonic.weather.common.models.Location
import io.chthonic.weather.common.models.Outcome
import io.chthonic.weather.domain.dataapi.LocationService
import io.chthonic.weather.domain.presentationapi.LocationRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepoImpl @Inject constructor(
    private val locationService: LocationService,
) : LocationRepo {
    override suspend fun getCurrentLocation(): Outcome<Location> {
        return locationService.getCurrentLocation()
    }
}
