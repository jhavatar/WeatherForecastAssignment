package io.chthonic.weather.presentation.screens.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import io.chthonic.weather.common.models.Outcome
import io.chthonic.weather.domain.presentationapi.WeatherRepo
import io.chthonic.weather.presentation.models.ListUiState
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = LocationDetailViewModel.LocationDetailViewModelFactory::class)
class LocationDetailViewModel @AssistedInject constructor(
    @Assisted("name") name: String,
    @Assisted("lat") lat: Double,
    @Assisted("lon") lon: Double,
    private val weatherRepo: WeatherRepo,
) : ViewModel() {

    @AssistedFactory
    interface LocationDetailViewModelFactory {
        fun create(
            @Assisted("name") name: String,
            @Assisted("lat") lat: Double,
            @Assisted("lon") lon: Double,
        ): LocationDetailViewModel
    }

    private val _state = MutableStateFlow(
        LocationDetailState(
            name = name,
            lat = lat,
            lon = lon,
        )
    )
    val state: StateFlow<LocationDetailState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            when (val outcome =
                weatherRepo.getWeekWeatherForecast(lat = state.value.lat, lon = state.value.lon)) {
                is Outcome.Success -> {
                    val dayWeatherList = outcome.data.map { it.toDayWeather() }
                    _state.update {
                        it.copy(
                            dayWeatherList = dayWeatherList.toImmutableList(),
                            listUiState = if (dayWeatherList.isNotEmpty()) ListUiState.Content else ListUiState.Empty,
                        )
                    }
                }

                is Outcome.Error -> {
                    _state.update {
                        it.copy(
                            listUiState = ListUiState.Error,
                        )
                    }
                }
            }
        }
    }

    fun onToggleTemperatureUnits() {
        _state.update {
            it.copy(
                temperatureUnits = it.temperatureUnits.toggle(),
            )
        }
    }
}