package io.chthonic.weather.feature.cityforecast

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.chthonic.weather.feature.cityforecast.screens.location.LocationDetailScreen
import io.chthonic.weather.feature.cityforecast.screens.locationlist.LocationListScreen
import io.chthonic.weather.ui.common.AppContainerState

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.cityForecastNavGraph(
    appContainerState: AppContainerState,
) {
    composable(
        route = CityForecastDestination.LocationList.route,
    ) {
        LocationListScreen(
            appContainerState = appContainerState,
        )
    }

    composable(
        route = CityForecastDestination.LocationDetail.route,
        arguments = CityForecastDestination.LocationDetail.arguments,
    ) { backStackEntry ->
        val name =
            CityForecastDestination.LocationDetail.getName(backStackEntry.arguments)
                ?: return@composable
        val lat =
            CityForecastDestination.LocationDetail.getLat(backStackEntry.arguments)
                ?: return@composable
        val lon =
            CityForecastDestination.LocationDetail.getLon(backStackEntry.arguments)
                ?: return@composable

        LocationDetailScreen(
            name = name,
            lat = lat,
            lon = lon,
            appContainerState = appContainerState,
        )
    }
}