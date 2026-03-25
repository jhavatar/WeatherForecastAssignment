package io.chthonic.weather.ui.common

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Stable
class AppContainerState(
    val navController: NavHostController,
    val snackbarHostState: SnackbarHostState,
    private val snackbarScope: CoroutineScope,
) {
    var appBarTitle by mutableStateOf<String?>(null)
        private set

    var appBarStyle by mutableStateOf(AppBarStyle.Pinned)
        private set

    var appBarShowNavigationIcon by mutableStateOf(false)
        private set

    var appBarActions by mutableStateOf<(@Composable RowScope.() -> Unit)?>(null)
        private set

    @Composable
    fun updateAppBarForDestination(
        destinationRoute: String, // prevent screens/destinations fighting over state during transitions
        title: String? = null,
        showNavigationIcon: Boolean = false,
        style: AppBarStyle = AppBarStyle.Pinned,
        actions: (@Composable RowScope.() -> Unit)? = null,
    ) {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        if (currentRoute != destinationRoute) return

        appBarTitle = title
        appBarStyle = style
        appBarShowNavigationIcon = showNavigationIcon
        appBarActions = actions
    }

    fun showSnackbar(message: String, duration: SnackbarDuration = SnackbarDuration.Short) {
        snackbarScope.launch {
            snackbarHostState.showSnackbar(
                message = message,
                duration = duration,
            )
        }
    }
}

enum class AppBarStyle {
    Pinned,       // TopAppBar, always visible
    Large,        // LargeTopAppBar, collapses on scroll
}

@Composable
fun rememberAppContainerState(
    navController: NavHostController = rememberNavController(),
    snackbarScope: CoroutineScope = rememberCoroutineScope(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
): AppContainerState = remember(navController, snackbarScope, snackbarHostState) {
    AppContainerState(
        navController = navController,
        snackbarHostState = snackbarHostState,
        snackbarScope = snackbarScope,
    )
}