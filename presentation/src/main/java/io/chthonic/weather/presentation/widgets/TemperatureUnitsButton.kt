package io.chthonic.weather.presentation.widgets

import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.chthonic.weather.presentation.models.TemperatureUnits

@Composable
fun TemperatureUnitsButton(
    temperatureUnits: TemperatureUnits,
    onToggleTemperatureUnits: () -> Unit,
) {
    TextButton(
        shape = CircleShape,
        colors = ButtonDefaults.textButtonColors(
            containerColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f),
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
        onClick = onToggleTemperatureUnits,
        modifier = Modifier.sizeIn(minWidth = 40.dp, minHeight = 40.dp)
    ) {
        Text(
            text = temperatureUnits.toStringShort(),
            style = MaterialTheme.typography.labelLarge,
        )
    }
}