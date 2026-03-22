package io.chthonic.weather.presentation.screens.location

import io.chthonic.weather.presentation.models.ListUiState
import io.chthonic.weather.presentation.models.TemperatureUnits

data class LocationDetailState(
    val name: String,
    val lat: Double,
    val lon: Double,
    val listUiState: ListUiState = ListUiState.Loading,
    val temperatureUnits: TemperatureUnits = TemperatureUnits.CELSIUS,
    val dayWeatherList: List<DayWeather> = emptyList(),
)