package io.chthonic.weather.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Preview
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AppContainer() {
    val appContainerState = rememberAppContainerState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            val appBarTitle = appContainerState.showAppBarTitle.collectAsStateWithLifecycle()
            TopAppBar(
                title = { Text(appBarTitle.value ?: "") },
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