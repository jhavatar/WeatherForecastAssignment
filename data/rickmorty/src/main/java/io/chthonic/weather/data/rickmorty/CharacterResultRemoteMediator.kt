package io.chthonic.weather.data.rickmorty

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import dagger.Lazy
import io.chthonic.weather.data.rickmorty.database.CharactersDao
import io.chthonic.weather.data.rickmorty.database.RickMortyDatabase
import io.chthonic.weather.data.rickmorty.database.models.CharacterInfoDb
import io.chthonic.weather.data.rickmorty.rest.RickMortyRestApi
import io.chthonic.weather.data.rickmorty.rest.models.CharacterResult
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

private const val MAX_RETRY_COUNT = 3
const val PAGE_SIZE = 20

@OptIn(ExperimentalPagingApi::class)
class CharacterResultRemoteMediator @Inject constructor(
    private val database: RickMortyDatabase,
    private val api: Lazy<RickMortyRestApi>,
    private val charactersDao: CharactersDao
) : RemoteMediator<Int, CharacterInfoDb>() {

    override suspend fun initialize(): InitializeAction =
        if (charactersDao.getCharacterCount() > 0) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterInfoDb>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )

                LoadType.REFRESH -> 1
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    lastItem?.id?.let { it / PAGE_SIZE + 1 } ?: 1
                }
            }

            val response = getCharacters(page).mapToValidCharacterData()
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    charactersDao.clearAll()
                }
                charactersDao.insertAll(response)
            }
            MediatorResult.Success(
                endOfPaginationReached = response.isEmpty()
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getCharacters(pageNumber: Int): List<CharacterResult> =
        io.chthonic.weather.data.common.io.retryIO(times = MAX_RETRY_COUNT) {
            api.get().getCharacters(pageNumber).results
        }

    private fun List<CharacterResult>.mapToValidCharacterData(): List<CharacterInfoDb> = mapNotNull {
        if (it.id != null && it.name != null && it.image != null) {
            CharacterInfoDb(id = it.id, name = it.name, image = it.image)
        } else {
            null
        }
    }
}
