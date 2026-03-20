package io.chthonic.weather.presentation.nav

import android.os.Bundle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Destination(val route: String, val arguments: List<NamedNavArgument> = emptyList()) {

    companion object {
        private const val CHAR_ID_ARGUMENT: String = "charId"
    }

    data object CharacterList : Destination(route = "characterList")

    data object Character :
        Destination(
            route = "character/{$CHAR_ID_ARGUMENT}",
            arguments = listOf(navArgument(CHAR_ID_ARGUMENT) {
                type = NavType.IntType
            }),
        ) {

        fun buildUniqueRoute(charId: Int): String =
            route.replace("{$CHAR_ID_ARGUMENT}", charId.toString())

        fun getCharId(arguments: Bundle?): Int? = arguments?.getInt(CHAR_ID_ARGUMENT)
    }
}