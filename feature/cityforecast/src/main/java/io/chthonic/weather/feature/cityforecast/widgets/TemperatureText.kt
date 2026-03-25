package io.chthonic.weather.feature.cityforecast.widgets

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import io.chthonic.weather.feature.cityforecast.models.TemperatureUnits
import io.chthonic.weather.feature.cityforecast.models.cToF
import kotlin.math.roundToInt

@Composable
internal fun TemperatureText(
    temperature: Double?,
    units: TemperatureUnits,
    color: Color,
    valueTextStyle: TextStyle,
    unitsTextStyle: TextStyle,
    modifier: Modifier = Modifier,
    showDegrees: Boolean = true,
    showUnits: Boolean = true,
    nullString: String = "--",
) {
    AnimatedContent(
        targetState = units,
        transitionSpec = {
            scaleIn(initialScale = 0.8f) + fadeIn() togetherWith
                    scaleOut(targetScale = 0.8f) + fadeOut()
        },
        label = "units",
    ) { currentUnits ->

        val temperatureValue = when (currentUnits) {
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
                text = temperatureString,
                style = valueTextStyle,
                color = color,
            )

            when {
                showDegrees && showUnits -> currentUnits.toStringShort()
                showDegrees -> "°"
                showUnits -> currentUnits.toChar()
                else -> null
            }?.let {
                Text(
                    text = it,
                    style = unitsTextStyle,
                    color = color,
                )
            }
        }
    }
}