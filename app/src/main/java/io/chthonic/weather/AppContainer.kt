package io.chthonic.weather

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import io.chthonic.weather.ui.common.AppBarStyle
import io.chthonic.weather.ui.common.rememberAppContainerState
import io.chthonic.weather.ui.common.theme.AppColors

/**
 * Root composable that assembles the app shell — scaffold, app bar, snackbar host,and navigation host.
 * Note, [io.chthonic.weather.ui.common.AppContainerState], that lives in module :ui:common, allows modules to update AppContainer's UI
 * elements such as the app bar title, navigation icon, and actions — without depending on :app.
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AppContainer() {
    val appContainerState = rememberAppContainerState()
    val scrollBehavior = when (appContainerState.appBarStyle) {
        AppBarStyle.Pinned -> TopAppBarDefaults.pinnedScrollBehavior()
        AppBarStyle.Large -> TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    }
    val isDark = isSystemInDarkTheme()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            val appBarContainerColor = AppColors.appBarContainerColor(isDark)
            val titleContentColor = AppColors.appBarTitleContentColor(isDark)
            val navigationIcon = if (appContainerState.appBarShowNavigationIcon) {
                @Composable {
                    IconButton(onClick = { appContainerState.navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier,
                            tint = titleContentColor,
                        )
                    }
                }
            } else {
                {}
            }

            when (appContainerState.appBarStyle) {
                AppBarStyle.Pinned -> TopAppBar(
                    title = { Text(appContainerState.appBarTitle ?: "") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = appBarContainerColor,
                        titleContentColor = titleContentColor,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    ),
                    scrollBehavior = scrollBehavior,
                    navigationIcon = navigationIcon,
                    actions = appContainerState.appBarActions ?: {},
                )

                AppBarStyle.Large -> LargeTopAppBar(
                    title = { Text(appContainerState.appBarTitle ?: "") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = appBarContainerColor,
                        scrolledContainerColor = appBarContainerColor,
                        titleContentColor = titleContentColor,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    ),
                    scrollBehavior = scrollBehavior,
                    navigationIcon = navigationIcon,
                    actions = appContainerState.appBarActions ?: {},
                )
            }
        },
        snackbarHost = {
            SnackbarHost(appContainerState.snackbarHostState)
        },
        floatingActionButton = {},
        bottomBar = {},
    ) { innerPadding ->
        AppContainerNavHost(
            appContainerState = appContainerState,
            modifier = Modifier.padding(innerPadding),
        )
    }
}