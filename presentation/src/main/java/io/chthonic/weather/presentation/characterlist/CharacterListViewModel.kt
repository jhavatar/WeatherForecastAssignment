package io.chthonic.weather.presentation.characterlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import io.chthonic.weather.domain.presentationapi.GetCharacterListUseCase
import io.chthonic.weather.domain.presentationapi.models.CharacterInfo
import io.chthonic.weather.presentation.wrapper.SideEffect
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CharacterListViewModel @Inject constructor(
    getCharacterListUseCase: GetCharacterListUseCase
) : ViewModel() {

    val characterListToDisplay: Flow<PagingData<CharacterInfo>> =
        getCharacterListUseCase.execute()
            .cachedIn(viewModelScope)

    private val _navigateSideEffect = MutableStateFlow<SideEffect<NavigationTarget>?>(null)
    val navigateSideEffect: StateFlow<SideEffect<NavigationTarget>?> =
        _navigateSideEffect.asStateFlow()

    fun onCharacterClick(charInfo: CharacterInfo) {
        NavigationTarget.CharacterScreen(
            characterId = charInfo.id,
        ).let {
            _navigateSideEffect.value = SideEffect(it)
        }
    }

    sealed class NavigationTarget {
        data class CharacterScreen(
            val characterId: Int,
        ) : NavigationTarget()
    }
}