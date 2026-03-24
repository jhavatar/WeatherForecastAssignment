package io.chthonic.weather.presentation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.chthonic.weather.presentation.nav.Destination
import io.chthonic.weather.presentation.screens.location.LocationDetailScreen
import io.chthonic.weather.presentation.screens.locationlist.LocationListScreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppContainerNavHost(
    appContainerState: AppContainerState,
    modifier: Modifier,
) = SharedTransitionLayout(modifier = modifier) {
    NavHost(
        navController = appContainerState.navController,
        startDestination = Destination.LocationList.route,
        modifier = Modifier,
    ) {
        composable(
            route = Destination.LocationList.route,
        ) {
            LocationListScreen(
                appContainerState = appContainerState,
            )
        }
        
        composable(
            route = Destination.LocationDetail.route,
            arguments = Destination.LocationDetail.arguments,
        ) { backStackEntry ->
            val name =
                Destination.LocationDetail.getName(backStackEntry.arguments)
                    ?: return@composable
            val lat =
                Destination.LocationDetail.getLat(backStackEntry.arguments)
                    ?: return@composable
            val lon =
                Destination.LocationDetail.getLon(backStackEntry.arguments)
                    ?: return@composable

            LocationDetailScreen(
                name = name,
                lat = lat,
                lon = lon,
                appContainerState = appContainerState,
            )
        }
    }
}