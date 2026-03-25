package io.chthonic.weather.feature.cityforecast.screens.locationlist

import io.chthonic.weather.common.models.Outcome
import io.chthonic.weather.feature.cityforecast.models.ListUiState
import javax.inject.Inject

internal class LocationListUiStateResolver @Inject constructor() {

    fun resolveOnSearchAction(query: String): ListUiState =
        if (query.isBlank()) ListUiState.Idle else ListUiState.Loading

    fun resolveOnSearchResult(
        geocodingOutcome: Outcome<*>,
        locations: List<LocationCurrentWeather>,
        searchText: String,
    ): ListUiState {
        return when {
            geocodingOutcome is Outcome.Error -> ListUiState.Error
            locations.isNotEmpty() -> ListUiState.Content
            searchText.isBlank() -> ListUiState.Idle
            else -> ListUiState.Empty
        }
    }
}