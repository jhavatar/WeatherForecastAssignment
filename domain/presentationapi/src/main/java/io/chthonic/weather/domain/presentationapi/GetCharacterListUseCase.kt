package io.chthonic.weather.domain.presentationapi

import androidx.paging.PagingData
import io.chthonic.weather.domain.presentationapi.models.CharacterInfo
import kotlinx.coroutines.flow.Flow

interface GetCharacterListUseCase {
    fun execute(): Flow<PagingData<CharacterInfo>>
}