package io.chthonic.weather.feature.cityforecast

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.chthonic.weather.feature.cityforecast.screens.citydetail.CityDetailScreen
import io.chthonic.weather.feature.cityforecast.screens.citylist.CityListScreen
import io.chthonic.weather.ui.common.AppContainerState

/**
 * Registers the City Forecast feature's destinations on the nav graph ([androidx.navigation.NavHost].
 * Call this within a NavHost block to add the city list and city detail screens to the navigation graph.
 * The appContainerState is passed through to each screen to allow app bar and snackbar control.
 *
 * Usage:
 * `NavHost(
 *     navController = appContainerState.navController,
 *     startDestination = CityForecastDestination.CityList.route,
 * ) {
 *     cityForecastNavGraph(appContainerState)
 * }`
 */
@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.cityForecastNavGraph(
    appContainerState: AppContainerState,
) {
    composable(
        route = CityForecastDestination.CityList.route,
    ) {
        CityListScreen(
            appContainerState = appContainerState,
        )
    }

    composable(
        route = CityForecastDestination.CityDetail.route,
        arguments = CityForecastDestination.CityDetail.arguments,
    ) { backStackEntry ->
        val name =
            CityForecastDestination.CityDetail.getName(backStackEntry.arguments)
                ?: return@composable
        val lat =
            CityForecastDestination.CityDetail.getLat(backStackEntry.arguments)
                ?: return@composable
        val lon =
            CityForecastDestination.CityDetail.getLon(backStackEntry.arguments)
                ?: return@composable

        CityDetailScreen(
            name = name,
            lat = lat,
            lon = lon,
            appContainerState = appContainerState,
        )
    }
}