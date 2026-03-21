package io.chthonic.weather.presentation.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import io.chthonic.weather.presentation.models.TemperatureUnits
import io.chthonic.weather.presentation.models.cToF
import kotlin.math.roundToInt

@Composable
fun TemperatureText(
    temperature: Double?,
    units: TemperatureUnits,
    color: Color,
    valueTextStyle: TextStyle,
    otherTextStyle: TextStyle,
    showDegrees: Boolean = true,
    showUnits: Boolean = true,
    modifier: Modifier = Modifier,
    nullString: String = "--",
) {
    val temperatureValue = when (units) {
        TemperatureUnits.CELSIUS -> temperature
        TemperatureUnits.FAHRENHEIT -> temperature?.cToF()
    }

    temperatureValue?.toInt()

    val temperatureString = temperatureValue?.roundToInt()?.toString() ?: nullString

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Top,
    ) {
        Text(
            text = temperatureString.padStartToLength(3),
            style = valueTextStyle,
            color = color,
        )

        when {
            showDegrees && showUnits -> units.toStringShort()
            showDegrees -> "°"
            showUnits -> units.toChar()
            else -> null
        }?.let {
            Text(
                text = it,
                style = otherTextStyle,
                color = color,
            )
        }
    }
}

private fun String.padStartToLength(length: Int, padChar: Char = ' '): String =
    padStart(maxOf(length, this.length), padChar)