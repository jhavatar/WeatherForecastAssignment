package io.chthonic.weather.common.models

import androidx.compose.runtime.Immutable

@Immutable
data class Location(
    val lat: Double,
    val lon: Double,
) {

    // Should be unique for each location
    val key: String = "${lat}_${lon}"

    fun format(): String {
        return if (lat.isUnknown() || lon.isUnknown()) {
            "Location unavailable"
        } else {
            val lat = "%.4f° %s".format(Math.abs(lat), if (lat >= 0) "N" else "S")
            val lon = "%.4f° %s".format(Math.abs(lon), if (lon >= 0) "E" else "W")
            "$lat, $lon"
        }
    }

    companion object {
        const val UNKNOWN_COORD: Double = Double.NaN
    }
}

private fun Double.isUnknown(): Boolean = this.isNaN()