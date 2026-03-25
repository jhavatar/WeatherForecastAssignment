package io.chthonic.weather.ui.common.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

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
    val colorScheme = AppColorTheme.getColorScheme(isDarkTheme)

    CompositionLocalProvider(LocalSpacing provides spacing) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            shapes = AppShapes,
            content = content,
        )
    }
}