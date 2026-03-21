package io.chthonic.weather.presentation.nav

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

private const val LOC_LAT_ARG: String = "lat"
private const val LOC_LON_ARG: String = "lon"
private const val LOC_NAME_ARG: String = "name"

sealed class Destination(val route: String, val arguments: List<NamedNavArgument> = emptyList()) {

    data object LocationList : Destination(route = "locations")

    data object LocationDetail :
        Destination(
            route = "location/{$LOC_NAME_ARG}/{$LOC_LAT_ARG}/{$LOC_LON_ARG}",
            arguments = listOf(
                navArgument(LOC_NAME_ARG) { type = NavType.StringType },
                navArgument(LOC_LAT_ARG) { type = NavType.StringType },
                navArgument(LOC_LON_ARG) { type = NavType.StringType },

                )
        ) {

        fun buildUniqueRoute(name: String, lat: Double, lon: Double): String =
            route.replace("{$LOC_NAME_ARG}", Uri.encode(name))
                .replace("{$LOC_LAT_ARG}", lat.toString())
                .replace("{$LOC_LON_ARG}", lon.toString())

        fun getName(arguments: Bundle?): String? =
            arguments?.getString(LOC_NAME_ARG)?.let { Uri.decode(it) }

        fun getLat(arguments: Bundle?): Double? =
            arguments?.getString(LOC_LAT_ARG)?.toDouble()

        fun getLon(arguments: Bundle?): Double? =
            arguments?.getString(LOC_LON_ARG)?.toDoubleOrNull()
    }
}