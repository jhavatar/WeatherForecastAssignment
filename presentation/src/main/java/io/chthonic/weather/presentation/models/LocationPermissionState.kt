package io.chthonic.weather.presentation.models

enum class LocationPermissionState {
    Unknown, // not yet asked
    Granted,
    Denied, // denied once, can ask again
    PermanentlyDenied, // never ask again
    Skipped, // ask again next time
}