package io.chthonic.weather.feature.cityforecast.screens.citylist

import io.chthonic.weather.feature.cityforecast.models.ListUiState
import io.chthonic.weather.feature.cityforecast.models.LocationPermissionState
import io.chthonic.weather.feature.cityforecast.models.TemperatureUnits
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal data class CityListState(
    val listUiState: ListUiState = ListUiState.Idle,
    val locationPermissionState: LocationPermissionState = LocationPermissionState.Unknown,
    val searchText: String = "",
    val myLocation: LocationCurrentWeather? = null,
    val searchLocations: ImmutableList<LocationCurrentWeather> = persistentListOf(),
    val temperatureUnits: TemperatureUnits = TemperatureUnits.CELSIUS,
)