package io.chthonic.weather.data.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationListener
import android.location.LocationManager
import android.os.Looper
import dagger.hilt.android.qualifiers.ApplicationContext
import io.chthonic.weather.common.models.Location
import io.chthonic.weather.common.models.Outcome
import io.chthonic.weather.domain.dataapi.LocationService
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

internal class LocationServiceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : LocationService {

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): Outcome<Location> =
        suspendCancellableCoroutine { cont ->
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
                cont.resume(Outcome.Error("No location provider available"))
                return@suspendCancellableCoroutine
            }

            val lastKnown = locationManager.getLastKnownLocation(provider)
            if (lastKnown != null) {
                cont.resume(Outcome.Success(Location(lastKnown.latitude, lastKnown.longitude)))
                return@suspendCancellableCoroutine
            }

            val listener = object : LocationListener {
                override fun onLocationChanged(location: android.location.Location) {
                    locationManager.removeUpdates(this)
                    cont.resume(Outcome.Success(Location(location.latitude, location.longitude)))
                }

                override fun onProviderDisabled(provider: String) {
                    locationManager.removeUpdates(this)
                    cont.resume(Outcome.Error("Location provider disabled"))
                }
            }

            cont.invokeOnCancellation {
                locationManager.removeUpdates(listener)
            }

            locationManager.requestLocationUpdates(provider, 0L, 0f, listener, Looper.getMainLooper())
        }
}
