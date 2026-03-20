package io.chthonic.weather.presentation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.chthonic.weather.presentation.character.CharacterScreen
import io.chthonic.weather.presentation.characterlist.CharacterListScreen
import io.chthonic.weather.presentation.nav.Destination

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppContainerNavHost(
    appContainerState: AppContainerState,
    modifier: Modifier,
) = SharedTransitionLayout(modifier = modifier) {
    val sharedTransitionScope = this@SharedTransitionLayout
    NavHost(
        navController = appContainerState.navController,
        startDestination = Destination.CharacterList.route,
        modifier = Modifier,
    ) {
        composable(
            route = Destination.CharacterList.route,
        ) {
            CharacterListScreen(
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = this@composable,
                showSnackbar = appContainerState::showSnackbar,
                navController = appContainerState.navController,
                updateAppBarTitle = appContainerState::updateAppBarTitle
            )
        }
        composable(
            route = Destination.Character.route,
            arguments = Destination.Character.arguments,
        ) { backStackEntry ->
            val charId = Destination.Character.getCharId(backStackEntry.arguments)
            CharacterScreen(
                characterId = charId,
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = this@composable,
                updateAppBarTitle = appContainerState::updateAppBarTitle,
            )
        }
    }
}