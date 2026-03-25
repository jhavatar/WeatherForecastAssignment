package io.chthonic.weather.feature.cityforecast.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AcUnit
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.QuestionMark
import androidx.compose.material.icons.outlined.Thunderstorm
import androidx.compose.material.icons.outlined.Umbrella
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material.icons.outlined.WbCloudy
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material.icons.outlined.WbTwilight
import androidx.compose.ui.graphics.vector.ImageVector

internal enum class WeatherCondition(val description: String) {
    CLEAR_SKY("Clear sky"),
    MAINLY_CLEAR("Mainly clear"),
    PARTLY_CLOUDY("Partly cloudy"),
    OVERCAST("Overcast"),
    FOG("Fog"),
    RIME_FOG("Rime fog"),
    DRIZZLE_LIGHT("Light drizzle"),
    DRIZZLE_MODERATE("Moderate drizzle"),
    DRIZZLE_DENSE("Dense drizzle"),
    FREEZING_DRIZZLE_LIGHT("Light freezing drizzle"),
    FREEZING_DRIZZLE_DENSE("Dense freezing drizzle"),
    RAIN_SLIGHT("Slight rain"),
    RAIN_MODERATE("Moderate rain"),
    RAIN_HEAVY("Heavy rain"),
    FREEZING_RAIN_LIGHT("Light freezing rain"),
    FREEZING_RAIN_HEAVY("Heavy freezing rain"),
    SNOW_SLIGHT("Slight snow"),
    SNOW_MODERATE("Moderate snow"),
    SNOW_HEAVY("Heavy snow"),
    SNOW_GRAINS("Snow grains"),
    RAIN_SHOWERS_SLIGHT("Slight rain showers"),
    RAIN_SHOWERS_MODERATE("Moderate rain showers"),
    RAIN_SHOWERS_VIOLENT("Violent rain showers"),
    SNOW_SHOWERS_SLIGHT("Slight snow showers"),
    SNOW_SHOWERS_HEAVY("Heavy snow showers"),
    THUNDERSTORM("Thunderstorm"),
    THUNDERSTORM_HAIL_SLIGHT("Thunderstorm with slight hail"),
    THUNDERSTORM_HAIL_HEAVY("Thunderstorm with heavy hail"),
    UNKNOWN("Unknown"),
}

internal fun Int.toWeatherCondition(): WeatherCondition = when (this) {
    0 -> WeatherCondition.CLEAR_SKY
    1 -> WeatherCondition.MAINLY_CLEAR
    2 -> WeatherCondition.PARTLY_CLOUDY
    3 -> WeatherCondition.OVERCAST
    45 -> WeatherCondition.FOG
    48 -> WeatherCondition.RIME_FOG
    51 -> WeatherCondition.DRIZZLE_LIGHT
    53 -> WeatherCondition.DRIZZLE_MODERATE
    55 -> WeatherCondition.DRIZZLE_DENSE
    56 -> WeatherCondition.FREEZING_DRIZZLE_LIGHT
    57 -> WeatherCondition.FREEZING_DRIZZLE_DENSE
    61 -> WeatherCondition.RAIN_SLIGHT
    63 -> WeatherCondition.RAIN_MODERATE
    65 -> WeatherCondition.RAIN_HEAVY
    66 -> WeatherCondition.FREEZING_RAIN_LIGHT
    67 -> WeatherCondition.FREEZING_RAIN_HEAVY
    71 -> WeatherCondition.SNOW_SLIGHT
    73 -> WeatherCondition.SNOW_MODERATE
    75 -> WeatherCondition.SNOW_HEAVY
    77 -> WeatherCondition.SNOW_GRAINS
    80 -> WeatherCondition.RAIN_SHOWERS_SLIGHT
    81 -> WeatherCondition.RAIN_SHOWERS_MODERATE
    82 -> WeatherCondition.RAIN_SHOWERS_VIOLENT
    85 -> WeatherCondition.SNOW_SHOWERS_SLIGHT
    86 -> WeatherCondition.SNOW_SHOWERS_HEAVY
    95 -> WeatherCondition.THUNDERSTORM
    96 -> WeatherCondition.THUNDERSTORM_HAIL_SLIGHT
    99 -> WeatherCondition.THUNDERSTORM_HAIL_HEAVY
    else -> WeatherCondition.UNKNOWN
}

internal fun WeatherCondition.toIcon(): ImageVector = when (this) {
    WeatherCondition.CLEAR_SKY -> Icons.Outlined.WbSunny
    WeatherCondition.MAINLY_CLEAR -> Icons.Outlined.WbSunny
    WeatherCondition.PARTLY_CLOUDY -> Icons.Outlined.WbCloudy
    WeatherCondition.OVERCAST -> Icons.Outlined.Cloud
    WeatherCondition.FOG,
    WeatherCondition.RIME_FOG -> Icons.Outlined.WbTwilight  // best available substitute
    WeatherCondition.DRIZZLE_LIGHT,
    WeatherCondition.DRIZZLE_MODERATE,
    WeatherCondition.DRIZZLE_DENSE,
    WeatherCondition.FREEZING_DRIZZLE_LIGHT,
    WeatherCondition.FREEZING_DRIZZLE_DENSE -> Icons.Outlined.WaterDrop

    WeatherCondition.RAIN_SLIGHT,
    WeatherCondition.RAIN_MODERATE,
    WeatherCondition.RAIN_HEAVY,
    WeatherCondition.FREEZING_RAIN_LIGHT,
    WeatherCondition.FREEZING_RAIN_HEAVY,
    WeatherCondition.RAIN_SHOWERS_SLIGHT,
    WeatherCondition.RAIN_SHOWERS_MODERATE,
    WeatherCondition.RAIN_SHOWERS_VIOLENT -> Icons.Outlined.Umbrella

    WeatherCondition.SNOW_SLIGHT,
    WeatherCondition.SNOW_MODERATE,
    WeatherCondition.SNOW_HEAVY,
    WeatherCondition.SNOW_GRAINS,
    WeatherCondition.SNOW_SHOWERS_SLIGHT,
    WeatherCondition.SNOW_SHOWERS_HEAVY -> Icons.Outlined.AcUnit

    WeatherCondition.THUNDERSTORM,
    WeatherCondition.THUNDERSTORM_HAIL_SLIGHT,
    WeatherCondition.THUNDERSTORM_HAIL_HEAVY -> Icons.Outlined.Thunderstorm

    WeatherCondition.UNKNOWN -> Icons.Outlined.QuestionMark
}