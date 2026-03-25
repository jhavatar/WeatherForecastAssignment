package io.chthonic.weather.feature.cityforecast.screens.citylist

internal sealed interface NavigationEvent {
    data class ToCityDetail(
        val name: String,
        val lat: Double,
        val lon: Double
    ) : NavigationEvent
}