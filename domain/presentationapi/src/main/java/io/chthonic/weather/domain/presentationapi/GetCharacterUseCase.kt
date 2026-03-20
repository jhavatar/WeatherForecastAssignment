package io.chthonic.weather.domain.presentationapi

import io.chthonic.weather.domain.presentationapi.models.CharacterInfo

interface GetCharacterUseCase {
    suspend fun execute(characterId: Int): CharacterInfo?
}