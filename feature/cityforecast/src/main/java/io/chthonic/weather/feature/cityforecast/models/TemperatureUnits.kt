package io.chthonic.weather.feature.cityforecast.models


internal enum class TemperatureUnits {
    CELSIUS,
    FAHRENHEIT;

    override fun toString(): String {
        return when (this) {
            CELSIUS -> "°Celsius"
            FAHRENHEIT -> "°Fahrenheit"
        }
    }

    fun toStringShort(): String {
        return when (this) {
            CELSIUS -> "°C"
            FAHRENHEIT -> "°F"
        }
    }

    fun toChar(): String {
        return when (this) {
            CELSIUS -> "C"
            FAHRENHEIT -> "F"
        }
    }

    fun toggle(): TemperatureUnits = when (this) {
        CELSIUS -> FAHRENHEIT
        FAHRENHEIT -> CELSIUS
    }
}

/**
 * Assumes value is in Celsius and converts it to Fahrenheit
 * @return Temperature in Fahrenheit
 */
fun Double.cToF(): Double {
    return (this * 1.8) + 32.0
}