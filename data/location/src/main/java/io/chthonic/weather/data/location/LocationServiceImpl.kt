package io.chthonic.weather.data.location

import android.Manifest
import android.content.Context
import android.location.LocationListener
import android.location.LocationManager
import androidx.annotation.RequiresPermission
import dagger.hilt.android.qualifiers.ApplicationContext
import io.chthonic.weather.common.models.Location
import io.chthonic.weather.common.models.Outcome
import io.chthonic.weather.domain.dataapi.LocationService
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
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

        val provider = when {
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ->
                LocationManager.GPS_PROVIDER

            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ->
                LocationManager.NETWORK_PROVIDER

            else -> null
        }

        if (provider == null) {
            send(Outcome.Error("No location provider available"))
            close()
            return@callbackFlow
        }

        // emit last known immediately if recent
        val lastKnown = locationManager.getLastKnownLocation(provider)
        val isRecent = lastKnown != null &&
                (System.currentTimeMillis() - lastKnown.time) < 60_000L
        if (isRecent) {
            send(Outcome.Success(Location(lastKnown!!.latitude, lastKnown.longitude)))
        }

        val listener = object : LocationListener {
            override fun onLocationChanged(location: android.location.Location) {
                trySend(Outcome.Success(Location(location.latitude, location.longitude)))
            }

            override fun onProviderDisabled(provider: String) {
                trySend(Outcome.Error("Location provider disabled"))
                close()
            }
        }

        locationManager.requestLocationUpdates(
            provider,
            minUpdateIntervalMs,
            minUpdateDistanceM,
            listener,
            null,
        )

        awaitClose {
            locationManager.removeUpdates(listener)
        }
    }.distinctUntilChanged()
}
