package io.chthonic.weather

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import io.chthonic.weather.feature.cityforecast.cityForecastNavGraph
import io.chthonic.weather.feature.cityforecast.CityForecastDestination
import io.chthonic.weather.ui.common.AppContainerState

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppContainerNavHost(
    appContainerState: AppContainerState,
    modifier: Modifier,
) = SharedTransitionLayout(modifier = modifier) {
    NavHost(
        navController = appContainerState.navController,
        startDestination = CityForecastDestination.LocationList.route,
        modifier = Modifier,
    ) {
        cityForecastNavGraph(appContainerState)
    }
}