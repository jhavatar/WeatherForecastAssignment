package io.chthonic.weather.data.location

import android.Manifest
import android.content.Context
import android.location.LocationListener
import android.location.LocationManager
import android.os.HandlerThread
import androidx.annotation.RequiresPermission
import dagger.hilt.android.qualifiers.ApplicationContext
import io.chthonic.weather.common.models.Location
import io.chthonic.weather.common.models.Outcome
import io.chthonic.weather.domain.dataapi.LocationService
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import timber.log.Timber
import javax.inject.Inject

internal class LocationServiceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : LocationService {

    @RequiresPermission(
        anyOf = [
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        ]
    )
    override fun getLocationUpdates(
        minUpdateIntervalMs: Long,
        minUpdateDistanceM: Float,
    ): Flow<Outcome<Location>> = callbackFlow {
        val locationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val providers = buildList {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                add(LocationManager.GPS_PROVIDER)
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
                add(LocationManager.NETWORK_PROVIDER)
        }

        if (providers.isEmpty()) {
            send(Outcome.Error("No location provider available"))
            close()
            return@callbackFlow
        }

        // emit last known immediately if recent
        providers.firstNotNullOfOrNull { locationManager.getLastKnownLocation(it) }
            ?.let { lastKnown ->
                val lastKnownAge = System.currentTimeMillis() - lastKnown.time
                if (lastKnownAge > 60_000L) {
                    Timber.w("Note, getLocationUpdates: lastKnown is older that a minute: ${lastKnownAge / 1000} seconds")
                }
                send(Outcome.Success(Location(lastKnown.latitude, lastKnown.longitude)))
            }

        val listener = object : LocationListener {
            override fun onLocationChanged(location: android.location.Location) {
                Timber.v("onLocationChanged: location = $location")
                trySend(Outcome.Success(Location(location.latitude, location.longitude)))
            }

            override fun onProviderDisabled(provider: String) {
                val anyEnabled = providers.any { locationManager.isProviderEnabled(it) }
                if (!anyEnabled) {
                    trySend(Outcome.Error("All location providers disabled"))
                    close()
                }
            }
        }

        val locationHandlerThread = HandlerThread("LocationThread").also { it.start() }
        providers.forEach { provider ->
            try {
                locationManager.requestLocationUpdates(
                    provider,
                    minUpdateIntervalMs,
                    minUpdateDistanceM,
                    listener,
                    locationHandlerThread.looper,
                )
            } catch (e: Exception) {
                Timber.e(e, "requestLocationUpdates error for provider = $provider")
            }
        }

        awaitClose {
            locationManager.removeUpdates(listener)
            locationHandlerThread.quitSafely()
        }
    }.distinctUntilChanged()
}
