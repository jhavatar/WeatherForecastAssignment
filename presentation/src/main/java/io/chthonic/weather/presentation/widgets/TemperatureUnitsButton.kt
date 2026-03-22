package io.chthonic.weather.presentation.widgets

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import io.chthonic.weather.presentation.models.TemperatureUnits

@Composable
fun TemperatureUnitsButton(
    temperatureUnits: TemperatureUnits,
    onToggleTemperatureUnits: () -> Unit,
) {
    TextButton(
        shape = CircleShape,
        onClick = onToggleTemperatureUnits,
    ) {
        Text(
            text = temperatureUnits.toStringShort(),
            style = MaterialTheme.typography.labelLarge,
        )
    }
}