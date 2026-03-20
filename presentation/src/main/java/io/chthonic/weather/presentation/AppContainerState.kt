package io.chthonic.weather.presentation

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AppContainerState(
    val navController: NavHostController,
    val snackbarScope: CoroutineScope,
    val snackbarHostState: SnackbarHostState,
) {
    private val _showAppBarTitle = MutableStateFlow<String?>(null)
    val showAppBarTitle = _showAppBarTitle.asStateFlow()

    fun showSnackbar(message: String, duration: SnackbarDuration = SnackbarDuration.Short) {
        snackbarScope.launch {
            snackbarHostState.showSnackbar(
                message = message,
                duration = duration
            )
        }
    }

    fun updateAppBarTitle(title: String?) {
        _showAppBarTitle.value = title
    }
}

@Composable
fun rememberAppContainerState(
    navController: NavHostController = rememberNavController(),
    snackbarScope: CoroutineScope = rememberCoroutineScope(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) = remember(navController, snackbarScope, snackbarHostState) {
    AppContainerState(
        snackbarHostState = snackbarHostState,
        navController = navController,
        snackbarScope = snackbarScope,
    )
}