package io.chthonic.weather.feature.cityforecast.screens.citydetail

import io.chthonic.weather.feature.cityforecast.models.ListUiState
import io.chthonic.weather.feature.cityforecast.models.TemperatureUnits
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal data class CityDetailState(
    val name: String,
    val lat: Double,
    val lon: Double,
    val listUiState: ListUiState = ListUiState.Loading,
    val temperatureUnits: TemperatureUnits = TemperatureUnits.CELSIUS,
    val dayWeatherList: ImmutableList<DayWeather> = persistentListOf(),
)