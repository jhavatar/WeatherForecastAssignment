package io.chthonic.weather.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorsScheme = darkColorScheme(
    surface = Purple500,
    primary = Teal200,
    secondary = Teal700,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onSurface = Color.White,
)
private val LightColorsScheme = lightColorScheme(
    surface = Purple200,
    primary = Teal200,
    primaryContainer = Purple700,
    secondary = Teal700,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onSurface = Color.Black,
)

@Composable
fun AppTheme(
    isDarkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (isDarkTheme) DarkColorsScheme else LightColorsScheme,
        content = content
    )
}