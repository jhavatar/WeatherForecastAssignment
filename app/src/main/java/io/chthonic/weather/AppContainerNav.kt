package io.chthonic.weather

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import io.chthonic.weather.feature.cityforecast.CityForecastDestination
import io.chthonic.weather.feature.cityforecast.cityForecastNavGraph
import io.chthonic.weather.ui.common.AppContainerState

/**
 * Wires the nav graph and connects each destination to its screen composable.
 *
 * Note, In this implementation features have zero knowledge of each other
 * and only expose callback to the NavHost describing the intent.
 * e.g., navigate from the City Air Quality feature's screen to the City Forecast feature's screen:
 * `cityAirQualityNavGraph(
 *     appContainerState = appContainerState,
 *     onNavigateToCityForecast = { lat, lon, name ->
 *         appContainerState.navController.navigate(
 *             CityForecastDestination.CityDetail.buildUniqueRoute(lat, lon, name)
 *         )
 *     }
 * )`
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppContainerNavHost(
    appContainerState: AppContainerState,
    modifier: Modifier,
) = SharedTransitionLayout(modifier = modifier) {
    NavHost(
        navController = appContainerState.navController,
        startDestination = CityForecastDestination.CityList.route,
        modifier = Modifier,
    ) {
        cityForecastNavGraph(appContainerState)
    }
}