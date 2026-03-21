package io.chthonic.weather.presentation.screens.locationlist

sealed interface NavigationEvent {
    data class ToLocationDetail(
        val name: String,
        val lat: Double,
        val lon: Double
    ) : NavigationEvent
}