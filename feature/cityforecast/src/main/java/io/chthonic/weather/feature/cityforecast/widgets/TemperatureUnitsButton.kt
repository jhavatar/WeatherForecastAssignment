package io.chthonic.weather.feature.cityforecast.widgets

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.chthonic.weather.feature.cityforecast.models.TemperatureUnits
import io.chthonic.weather.ui.common.theme.AppColors

@Composable
internal fun TemperatureUnitsButton(
    temperatureUnits: TemperatureUnits,
    onToggleTemperatureUnits: () -> Unit,
) {
    val color = AppColors.appBarTitleContentColor(isSystemInDarkTheme())
    TextButton(
        shape = CircleShape,
        colors = ButtonDefaults.textButtonColors(
            containerColor = color.copy(alpha = 0.1f),
            contentColor = color,
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