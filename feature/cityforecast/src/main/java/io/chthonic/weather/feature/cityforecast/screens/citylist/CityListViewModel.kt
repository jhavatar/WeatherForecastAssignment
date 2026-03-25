package io.chthonic.weather.feature.cityforecast.screens.citylist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.chthonic.weather.common.models.Location
import io.chthonic.weather.common.models.Location.Companion.UNKNOWN_COORD
import io.chthonic.weather.common.models.Outcome
import io.chthonic.weather.domain.presentationapi.GeocodingRepo
import io.chthonic.weather.domain.presentationapi.LocationRepo
import io.chthonic.weather.domain.presentationapi.WeatherRepo
import io.chthonic.weather.feature.cityforecast.models.LocationPermissionState
import io.chthonic.weather.feature.cityforecast.models.toWeatherCondition
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
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
import timber.log.Timber
import javax.inject.Inject

private const val MY_LOCATION_DISPLAY_NAME = "MY LOCATION"

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
internal class CityListViewModel @Inject constructor(
    private val geocodingRepo: GeocodingRepo,
    private val weatherRepo: WeatherRepo,
    private val locationRepo: LocationRepo,
    private val listUiStateResolver: CityListUiStateResolver,
) : ViewModel() {

    private val _state = MutableStateFlow(CityListState())
    val state: StateFlow<CityListState> = _state.asStateFlow()

    private val _navigationEvent = Channel<NavigationEvent>(Channel.BUFFERED)
    val navigationEvent = _navigationEvent.receiveAsFlow()

    private var locationUpdatesJob: Job? = null
    private var localWeatherFetchJob: Job? = null

    init {
        viewModelScope.launch {
            _state.map { it.searchText }
                .debounce(250)
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    if (query.isBlank()) return@flatMapLatest flowOf(
                        Outcome.Success(emptyList())
                    )
                    flow {
                        val outcome = geocodingRepo.searchCity(query)
                        emit(outcome.map {
                            it.map { city ->
                                city.toLocationCurrentWeather()
                            }
                        })
                    }
                }
                .collect { geocodingOutcome: Outcome<List<LocationCurrentWeather>> ->
                    val locations: ImmutableList<LocationCurrentWeather> = when (geocodingOutcome) {
                        is Outcome.Success -> geocodingOutcome.data
                        is Outcome.Error -> emptyList()
                    }.toImmutableList()

                    // update state with locations immediately
                    _state.update {
                        it.copy(
                            searchLocations = locations,
                            listUiState = listUiStateResolver.resolveOnSearchResult(
                                geocodingOutcome = geocodingOutcome,
                                locations = locations,
                                searchText = state.value.searchText,
                            ),
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
                                    searchLocations = state.searchLocations.map {
                                        if (it.key == updated.key) updated else it
                                    }.toImmutableList(),
                                )
                            }
                        }
                    }.awaitAll()
                }
        }
    }

    fun onQueryChange(query: String) {
        _state.update {
            it.copy(
                searchText = query,
                listUiState = listUiStateResolver.resolveOnSearchAction(query),
            )
        }
    }

    fun onLocationClick(locationState: LocationCurrentWeather) {
        viewModelScope.launch {
            _navigationEvent.send(
                NavigationEvent.ToCityDetail(
                    name = locationState.displayName,
                    lat = locationState.location.lat,
                    lon = locationState.location.lon
                )
            )
        }
    }

    fun onToggleTemperatureUnits() {
        _state.update {
            it.copy(
                temperatureUnits = it.temperatureUnits.toggle(),
            )
        }
    }

    private fun startMyLocationUpdates() {
        if (locationUpdatesJob?.isActive == true) return
        locationUpdatesJob = viewModelScope.launch {
            locationRepo.getLocationUpdates()
                .collect { outcome ->
                    Timber.v("startMyLocationUpdates: outcome = $outcome")
                    when (outcome) {
                        is Outcome.Success -> {
                            _state.update {
                                it.copy(
                                    myLocation = LocationCurrentWeather(
                                        location = outcome.data,
                                        displayName = MY_LOCATION_DISPLAY_NAME,
                                    )
                                )
                            }
                            fetchWeatherForMyLocation(outcome.data)
                        }

                        is Outcome.Error -> {
                            _state.update {
                                it.copy(
                                    // if there was an error, keep the previous location
                                    myLocation = it.myLocation ?: LocationCurrentWeather(
                                        location = Location(
                                            lat = UNKNOWN_COORD,
                                            lon = UNKNOWN_COORD,
                                        ),
                                        displayName = MY_LOCATION_DISPLAY_NAME,
                                        weatherError = true,
                                    )
                                )
                            }
                        }
                    }
                }
        }
    }

    private fun fetchWeatherForMyLocation(location: Location) {
        localWeatherFetchJob?.cancel()
        localWeatherFetchJob = viewModelScope.launch {
            val outcome = weatherRepo.getCurrentWeatherForecast(location.lat, location.lon)
            val location = state.value.myLocation ?: return@launch
            Timber.v("fetchWeatherForLocation: outcome = $outcome")
            val updated = when (outcome) {
                is Outcome.Success -> location.copy(
                    temp = outcome.data.temp,
                    weatherCondition = outcome.data.weatherCode.toWeatherCondition(),
                    weatherError = false,
                )

                else -> location.copy(weatherError = true)
            }
            Timber.v("fetchWeatherForLocation: updated = $updated")
            _state.update { state ->
                state.copy(myLocation = updated)
            }
        }
    }


    fun onLocationPermissionGranted() {
        _state.update { it.copy(locationPermissionState = LocationPermissionState.Granted) }
        startMyLocationUpdates()
    }

    fun onLocationPermissionDenied(isPermanent: Boolean) {
        _state.update {
            it.copy(
                locationPermissionState = if (isPermanent) {
                    LocationPermissionState.PermanentlyDenied
                } else {
                    LocationPermissionState.Denied
                }
            )
        }
    }

    fun onLocationPermissionDismissed() {
        locationUpdatesJob?.cancel()
        locationUpdatesJob = null
        localWeatherFetchJob?.cancel()
        localWeatherFetchJob = null
        _state.update { it.copy(locationPermissionState = LocationPermissionState.Skipped) }
    }
}