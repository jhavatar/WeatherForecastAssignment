package io.chthonic.weather.domain.dataapi

import androidx.paging.PagingData
import io.chthonic.weather.domain.dataapi.models.CharacterInfo
import kotlinx.coroutines.flow.Flow

interface RickMortyRepository {
    suspend fun getCharacter(characterId: Int): CharacterInfo?
    fun getCharacters(): Flow<PagingData<CharacterInfo>>
}