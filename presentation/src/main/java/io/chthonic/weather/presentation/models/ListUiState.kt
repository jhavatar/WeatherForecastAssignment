package io.chthonic.weather.presentation.models

sealed interface ListUiState {
    data object Idle : ListUiState
    data object Loading : ListUiState
    data object Empty : ListUiState
    data object Error : ListUiState
    data object Content : ListUiState
}