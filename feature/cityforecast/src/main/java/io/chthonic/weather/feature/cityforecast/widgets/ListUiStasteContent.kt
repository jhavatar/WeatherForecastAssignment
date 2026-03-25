package io.chthonic.weather.feature.cityforecast.widgets

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.chthonic.weather.ui.common.theme.Spacing

internal const val EMPTY_CONTENT_KEY = "EmptyContent"
internal const val IDLE_CONTENT_KEY = "IdleContent"
internal const val ERROR_CONTENT_KEY = "ErrorContent"
internal const val LOADING_CONTENT_KEY = "LoadingContent"

@Composable
internal fun contentColor() = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)

@Composable
internal fun EmptyContent(
    spacing: Spacing,
    modifier: Modifier = Modifier,
    text: String = "No cities match your search.",
    icon: ImageVector = Icons.Outlined.SearchOff,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            tint = contentColor(),
        )

        Text(
            text = text,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(spacing.m),
            color = contentColor(),
        )
    }
}

@Composable
internal fun ErrorContent(
    text: String = "Error: something went wrong.",
    spacing: Spacing,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            Icons.Outlined.ErrorOutline,
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            tint = contentColor(),
        )

        Text(
            text = text,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(spacing.m),
            color = contentColor(),
        )
    }
}

@Composable
internal fun LoadingContent(spacing: Spacing, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val alpha by rememberInfiniteTransition(label = "loading")
            .animateFloat(
                initialValue = 0.3f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(800),
                    repeatMode = RepeatMode.Reverse,
                ),
                label = "alpha",
            )

        CircularProgressIndicator(
            strokeWidth = 2.dp,
            color = MaterialTheme.colorScheme.primary.copy(alpha = alpha),
            modifier = Modifier.size(36.dp),
        )

        Text(
            text = "Fetching weather...",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(spacing.m),
            color = contentColor(),
        )
    }
}