package io.chthonic.weather.presentation

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

    fun updateAppBarTitle(title: String?) {
        appBarTitle = title
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