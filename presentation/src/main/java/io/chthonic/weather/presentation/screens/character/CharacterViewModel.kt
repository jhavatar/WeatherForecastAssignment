package io.chthonic.weather.presentation.screens.character

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import io.chthonic.weather.domain.presentationapi.GetCharacterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = CharacterViewModel.CharacterViewModelFactory::class)
class CharacterViewModel(
    private val characterId: Int?,
    getCharacterUsecase: GetCharacterUseCase,
    initStateState: State,
) : ViewModel() {

    @AssistedInject
    constructor(
        @Assisted characterId: Int?,
        getCharacterUsecase: GetCharacterUseCase,
    ) : this(
        characterId = characterId,
        getCharacterUsecase = getCharacterUsecase,
        initStateState = State()
    )

    @AssistedFactory
    interface CharacterViewModelFactory {
        fun create(characterId: Int?): CharacterViewModel
    }

    data class State(
        val imageUrlToShow: String = "",
        val titleToShow: String = ""
    )

    private val _state = MutableStateFlow(initStateState)
    val state: StateFlow<State> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            characterId?.let { charId ->
                getCharacterUsecase.execute(charId)?.let {
                    _state.value = state.value.copy(
                        titleToShow = it.name,
                        imageUrlToShow = it.image,
                    )
                }
            }
        }
    }
}