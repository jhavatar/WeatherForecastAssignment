package io.chthonic.weather.common.models

data class Location(
    val lat: Double,
    val lon: Double,
) {
    fun format(): String {
        val lat = "%.4f° %s".format(Math.abs(lat), if (lat >= 0) "N" else "S")
        val lon = "%.4f° %s".format(Math.abs(lon), if (lon >= 0) "E" else "W")
        return "$lat, $lon"
    }
}