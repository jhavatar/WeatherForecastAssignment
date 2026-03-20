package io.chthonic.weather.domain

import androidx.paging.PagingData
import androidx.paging.map
import io.chthonic.weather.domain.presentationapi.GetCharacterListUseCase
import io.chthonic.weather.domain.presentationapi.models.CharacterInfo
import io.chthonic.weather.domain.dataapi.RickMortyRepository
import io.chthonic.weather.domain.ktx.toPresentationModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class GetCharacterListUseCaseImpl @Inject constructor(
    private val rickMortyRepository: RickMortyRepository
) : GetCharacterListUseCase {
    override fun execute(): Flow<PagingData<CharacterInfo>> =
        rickMortyRepository.getCharacters().map {
            it.map { info ->
                info.toPresentationModel()
            }
        }
}