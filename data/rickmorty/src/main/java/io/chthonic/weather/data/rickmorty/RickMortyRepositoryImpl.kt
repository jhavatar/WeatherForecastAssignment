package io.chthonic.weather.data.rickmorty

import androidx.paging.*
import io.chthonic.weather.data.rickmorty.database.CharactersDao
import io.chthonic.weather.data.rickmorty.database.models.CharacterInfoDb
import io.chthonic.weather.domain.dataapi.RickMortyRepository
import io.chthonic.weather.domain.dataapi.models.CharacterInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class RickMortyRepositoryImpl @Inject constructor(
    private val characterResultRemoteMediator: CharacterResultRemoteMediator,
    private val charactersDao: CharactersDao
) : RickMortyRepository {

    override suspend fun getCharacter(characterId: Int): CharacterInfo? =
        charactersDao.getCharacter(characterId)?.toDomainModel()

    @OptIn(ExperimentalPagingApi::class)
    override fun getCharacters(): Flow<PagingData<CharacterInfo>> =
        Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
            ),
            remoteMediator = characterResultRemoteMediator
        ) {
            charactersDao.pagingSource()
        }.flow.map { pagingData ->
            pagingData.map {
                it.toDomainModel()
            }
        }

    private fun CharacterInfoDb.toDomainModel() =
        CharacterInfo(
            id = id,
            name = name,
            image = image
        )
}