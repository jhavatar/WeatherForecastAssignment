package io.chthonic.weather.presentation.character

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import io.chthonic.weather.presentation.R

@Composable
@OptIn(ExperimentalSharedTransitionApi::class)
fun CharacterScreen(
    characterId: Int?,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    updateAppBarTitle: (String?) -> Unit,
) {
    val viewModel =
        hiltViewModel<CharacterViewModel, CharacterViewModel.CharacterViewModelFactory> { factory ->
            factory.create(characterId)
        }

    val state = viewModel.state.collectAsStateWithLifecycle().value

    LaunchedEffect(state.titleToShow) {
        updateAppBarTitle(state.titleToShow)
    }

    CharacterScreenContent(
        state.imageUrlToShow,
        sharedTransitionScope,
        animatedContentScope,
    )
}

@Composable
@OptIn(ExperimentalSharedTransitionApi::class)
private fun CharacterScreenContent(
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