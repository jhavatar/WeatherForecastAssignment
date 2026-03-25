package io.chthonic.weather.feature.cityforecast.screens.locationlist

internal sealed interface NavigationEvent {
    data class ToLocationDetail(
        val name: String,
        val lat: Double,
        val lon: Double
    ) : NavigationEvent
}