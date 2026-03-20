package io.chthonic.weather.domain.ktx

import io.chthonic.weather.domain.presentationapi.models.CharacterInfo

fun io.chthonic.weather.domain.dataapi.models.CharacterInfo.toPresentationModel() =
    CharacterInfo(
        id = id,
        name = name,
        image = image,
    )