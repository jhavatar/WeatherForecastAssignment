package io.chthonic.weather.data.rickmorty.rest.models

import androidx.annotation.Keep


@Keep
data class CharacterResults(
    val results: List<CharacterResult>
)

@Keep
data class CharacterResult(
    val id: Int?,
    val name: String?,
    val image: String?,
)