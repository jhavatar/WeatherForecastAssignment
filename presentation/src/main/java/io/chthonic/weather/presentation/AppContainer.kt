package io.chthonic.weather.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Preview
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AppContainer() {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val appContainerState = rememberAppContainerState()
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            val appBarTitle = appContainerState.showAppBarTitle.collectAsStateWithLifecycle()
            LargeTopAppBar(
                title = { Text(appBarTitle.value ?: "") },
                scrollBehavior = scrollBehavior,
            )
        },
        floatingActionButton = {
        },
        bottomBar = {
        },
    ) { innerPadding ->
        AppContainerNavHost(
            appContainerState = appContainerState,
            modifier = Modifier.padding(innerPadding),
        )
    }
}