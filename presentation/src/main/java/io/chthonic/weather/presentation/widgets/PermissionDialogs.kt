package io.chthonic.weather.presentation.widgets

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOff
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.chthonic.weather.presentation.theme.LocalSpacing

@Composable
fun DeniedPermissionDialog(
    onRetry: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Outlined.LocationOff,
                contentDescription = null,
            )
        },
        title = {
            Text("Location access needed")
        },
        text = {
            Text("We need your location to show local weather conditions.")
        },
        confirmButton = {
            Button(onClick = onRetry) {
                Text("Grant Permission")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Not now")
            }
        },
    )
}

@Composable
fun PermanentlyDeniedPermissionDialog(
    onOpenSettings: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Outlined.LocationOff,
                contentDescription = null,
            )
        },
        title = {
            Text("Location permission blocked")
        },
        text = {
            Text("Location access has been permanently denied. Please enable it in app settings to show local weather.")
        },
        confirmButton = {
            Button(onClick = onOpenSettings) {
                Icon(Icons.Outlined.Settings, contentDescription = null)
                Spacer(Modifier.width(LocalSpacing.current.s))
                Text("Open Settings")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Not now")
            }
        },
    )
}