package io.chthonic.weather.presentation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.currentStateAsState
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.chthonic.weather.presentation.nav.Destination
import io.chthonic.weather.presentation.screens.location.LocationDetailScreen
import io.chthonic.weather.presentation.screens.locationlist.LocationListScreen
import kotlinx.coroutines.CoroutineScope

/**
 * A [CompositionLocal] that provides the [LifecycleOwner] of the current navigation
 * back stack entry. Unlike [LocalLifecycleOwner], which reflects the Activity lifecycle,
 * this is scoped to the current navigation destination — it resumes when the destination
 * becomes the top of the back stack and pauses when the user navigates away from it.
 * Use this when you need to react to back navigation in addition to initial composition,
 * without triggering on app foregrounding/backgrounding.
 */
val LocalNavLifecycleOwner = compositionLocalOf<LifecycleOwner> {
    error("No NavLifecycleOwner provided")
}

@Composable
fun LaunchedEffectOnNavLifecycleState(
    state: Lifecycle.State,
    block: suspend CoroutineScope.() -> Unit,
) {
    val navLifecycleOwner = LocalNavLifecycleOwner.current
    val lifecycleState by navLifecycleOwner.lifecycle.currentStateAsState()
    LaunchedEffect(lifecycleState == state) {
        if (lifecycleState == state) {
            block()
        }
    }
}

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
        composableWithLocalNavLifecycle(
            route = Destination.LocationList.route,
        ) {
            LocationListScreen(
                appContainerState = appContainerState,
            )
        }
        composableWithLocalNavLifecycle(
            route = Destination.LocationDetail.route,
            arguments = Destination.LocationDetail.arguments,
        ) { backStackEntry ->
            val name =
                Destination.LocationDetail.getName(backStackEntry.arguments)
                    ?: return@composableWithLocalNavLifecycle
            val lat =
                Destination.LocationDetail.getLat(backStackEntry.arguments)
                    ?: return@composableWithLocalNavLifecycle
            val lon =
                Destination.LocationDetail.getLon(backStackEntry.arguments)
                    ?: return@composableWithLocalNavLifecycle

            LocationDetailScreen(
                name = name,
                lat = lat,
                lon = lon,
                appContainerState = appContainerState,
            )
        }
    }
}

fun NavGraphBuilder.composableWithLocalNavLifecycle(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) {
    composable(route = route, arguments = arguments) { backStackEntry ->
        CompositionLocalProvider(LocalNavLifecycleOwner provides backStackEntry) {
            content(backStackEntry)
        }
    }
}