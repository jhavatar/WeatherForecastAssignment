package io.chthonic.weather.feature.cityforecast

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.chthonic.weather.feature.cityforecast.screens.citydetail.CityDetailScreen
import io.chthonic.weather.feature.cityforecast.screens.citylist.CityListScreen
import io.chthonic.weather.ui.common.AppContainerState

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