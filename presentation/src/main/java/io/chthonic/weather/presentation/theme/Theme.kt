package io.chthonic.weather.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

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
    val configuration = LocalConfiguration.current
    val spacing = when {
        configuration.screenWidthDp < 360 -> Spacing(
            xs = 2.dp,
            s = 6.dp,
            m = 12.dp,
            l = 18.dp,
            xl = 24.dp,
            xxl = 36.dp,
        )

        else -> Spacing() // default
    }


    CompositionLocalProvider(LocalSpacing provides spacing) {
        MaterialTheme(
            colorScheme = if (isDarkTheme) DarkColorsScheme else LightColorsScheme,
            content = content
        )
    }
}