package io.chthonic.weather.presentation.screens.locationlist

import io.chthonic.weather.presentation.models.ListUiState
import io.chthonic.weather.presentation.models.LocationPermissionState
import io.chthonic.weather.presentation.models.TemperatureUnits
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class LocationListState(
    val listUiState: ListUiState = ListUiState.Idle,
    val locationPermissionState: LocationPermissionState = LocationPermissionState.Unknown,
    val searchText: String = "",
    val myLocation: LocationCurrentWeather? = null,
    val searchLocations: ImmutableList<LocationCurrentWeather> = persistentListOf(),
    val temperatureUnits: TemperatureUnits = TemperatureUnits.CELSIUS,
)