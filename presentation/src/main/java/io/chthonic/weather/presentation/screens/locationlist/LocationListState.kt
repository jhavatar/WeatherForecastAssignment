package io.chthonic.weather.presentation.screens.locationlist

import io.chthonic.weather.common.models.Location
import io.chthonic.weather.presentation.models.ListUiState
import io.chthonic.weather.presentation.models.TemperatureUnits
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class LocationListState(
    val listUiState: ListUiState = ListUiState.Content,
    val searchText: String = "",
    val currentLocation: Location? = null,
    val locations: ImmutableList<LocationCurrentWeather> = persistentListOf(),
    val temperatureUnits: TemperatureUnits = TemperatureUnits.CELSIUS,
)