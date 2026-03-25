package io.chthonic.weather.feature.cityforecast.widgets

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import io.chthonic.weather.feature.cityforecast.models.LocationPermissionState

internal fun Context.checkLocationPermission(onGranted: () -> Unit) {
    val granted = ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION,
    ) == PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION,
    ) == PackageManager.PERMISSION_GRANTED

    if (granted) onGranted()
}

@Composable
internal fun rememberLocationPermissionLauncher(
    onGranted: () -> Unit,
    onDenied: (isPermanent: Boolean) -> Unit,
): ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>> {
    val context = LocalContext.current

    return rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
                || permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        if (granted) {
            onGranted()
        } else {
            val isPermanent = (context as? Activity)?.let {
                !ActivityCompat.shouldShowRequestPermissionRationale(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                )
            } ?: false
            onDenied(isPermanent)
        }
    }
}

private val locationPermissions = arrayOf(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION,
)

@Composable
internal fun RequestLocationPermission(
    permissionLauncher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>,
    onGranted: () -> Unit
) {
    val context = LocalContext.current

    val alreadyGranted = remember {
        ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    LaunchedEffect(Unit) {
        if (alreadyGranted) onGranted()
        else permissionLauncher.launch(locationPermissions)
    }
}

@Composable
internal fun HandleLocationPermissionState(
    locationPermissionState: LocationPermissionState,
    onGranted: () -> Unit,
    onDenied: (isPermanent: Boolean) -> Unit,
    onDismissed: () -> Unit,
    context: Context = LocalContext.current,
) {
    val permissionLauncher = rememberLocationPermissionLauncher(
        onGranted = onGranted,
        onDenied = onDenied,
    )
    when (locationPermissionState) {
        LocationPermissionState.Unknown -> {
            RequestLocationPermission(
                permissionLauncher = permissionLauncher,
                onGranted = onGranted,
            )
        }

        LocationPermissionState.Denied -> {
            DeniedPermissionDialog(
                onRetry = {
                    permissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                        )
                    )
                },
                onDismiss = onDismissed,
            )
        }

        LocationPermissionState.PermanentlyDenied -> {
            PermanentlyDeniedPermissionDialog(
                onOpenSettings = {
                    context.startActivity(
                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        }
                    )
                },
                onDismiss = onDismissed,
            )
        }

        LocationPermissionState.Granted, LocationPermissionState.Skipped -> {
            // show weather content
        }
    }
}