package io.chthonic.weather.presentation.screens.locationlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.chthonic.weather.common.models.Outcome
import io.chthonic.weather.domain.presentationapi.GeocodingRepo
import io.chthonic.weather.domain.presentationapi.WeatherRepo
import io.chthonic.weather.presentation.models.toWeatherCondition
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class LocationListViewModel @Inject constructor(
    private val geocodingRepo: GeocodingRepo,
    private val weatherRepo: WeatherRepo,
) : ViewModel() {

    private val _state = MutableStateFlow(LocationListState())
    val state: StateFlow<LocationListState> = _state.asStateFlow()

    private val _navigationEvent = Channel<NavigationEvent>(Channel.BUFFERED)
    val navigationEvent = _navigationEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            _state.map { it.searchText }
                .debounce(250)
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    if (query.isBlank()) return@flatMapLatest flowOf(persistentListOf())
                    flow {
                        when (val outcome = geocodingRepo.searchCity(query)) {
                            is Outcome.Success -> emit(
                                outcome.data.map {
                                    it.toLocationCurrentWeather()
                                }.toImmutableList()
                            )

                            else -> emit(persistentListOf())
                        }
                    }
                }
                .collect { locations ->
                    // update state with locations immediately
                    _state.update {
                        it.copy(
                            locations = locations
                        )
                    }

                    // now fetch weather for each location concurrently
                    locations.map { location ->
                        async {
                            val outcome = weatherRepo.getCurrentWeatherForecast(
                                lat = location.location.lat,
                                lon = location.location.lon,
                            )
                            val updated = when (outcome) {
                                is Outcome.Success -> location.copy(
                                    temp = outcome.data.temp,
                                    weatherCondition = outcome.data.weatherCode.toWeatherCondition()
                                )

                                else -> location.copy(weatherError = true)
                            }
                            // update just this location in state
                            _state.update { state ->
                                state.copy(
                                    locations = state.locations.map {
                                        if (it.location.hashCode() == updated.location.hashCode()) updated else it
                                    }.toImmutableList(),
                                )
                            }
                        }
                    }.awaitAll()
                }
        }
    }

    fun onQueryChange(query: String) {
        _state.update { it.copy(searchText = query) }
    }

    fun onLocationClick(locationState: LocationCurrentWeather) {
        viewModelScope.launch {
            _navigationEvent.send(
                NavigationEvent.ToLocationDetail(
                    name = locationState.displayName,
                    lat = locationState.location.lat,
                    lon = locationState.location.lon
                )
            )
        }
    }

    sealed class NavigationTarget {
        data class CharacterScreen(
            val characterId: Int,
        ) : NavigationTarget()
    }
}