package io.chthonic.weather.presentation.screens.location

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import io.chthonic.weather.presentation.AppBarStyle
import io.chthonic.weather.presentation.AppContainerState
import io.chthonic.weather.presentation.R

@Composable
@OptIn(ExperimentalSharedTransitionApi::class)
fun LocationDetailScreen(
    name: String,
    lat: Double,
    lon: Double,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    appContainerState: AppContainerState,
) {
    val viewModel =
        hiltViewModel<LocationDetailViewModel, LocationDetailViewModel.LocationDetailViewModelFactory> { factory ->
            factory.create(
                name = name,
                lat = lat,
                lon = lon,
            )
        }

    val state = viewModel.state.collectAsStateWithLifecycle().value

    LaunchedEffect(Unit) {
        appContainerState.updateAppBar(
            title = state.name,
            showNavigationIcon = true,
            style = AppBarStyle.Large,
        )
    }

//    LocationDetailScreenContent(
//        state.imageUrlToShow,
//        sharedTransitionScope,
//        animatedContentScope,
//    )
}

@Composable
@OptIn(ExperimentalSharedTransitionApi::class)
private fun LocationDetailScreenContent(
    url: String,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
) {
    with(sharedTransitionScope) {
        AsyncImage(
            model = url,
            placeholder = painterResource(R.drawable.rickmortyplaceholder),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color.Black)
                .sharedElement(
                    rememberSharedContentState(key = "image-$url"),
                    animatedVisibilityScope = animatedContentScope,
                ),
        )
    }
}