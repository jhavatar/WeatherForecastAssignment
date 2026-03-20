package io.chthonic.weather.domain

import io.chthonic.weather.domain.dataapi.RickMortyRepository
import io.chthonic.weather.domain.ktx.toPresentationModel
import io.chthonic.weather.domain.presentationapi.GetCharacterUseCase
import io.chthonic.weather.domain.presentationapi.models.CharacterInfo
import javax.inject.Inject

internal class GetCharacterUseCaseImpl @Inject constructor(
    private val rickMortyRepository: RickMortyRepository
) : GetCharacterUseCase {
    override suspend fun execute(characterId: Int): CharacterInfo? =
        rickMortyRepository.getCharacter(characterId)?.toPresentationModel()
}