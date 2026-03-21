package io.chthonic.weather.presentation.screens.location

import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel(assistedFactory = LocationDetailViewModel.LocationDetailViewModelFactory::class)
class LocationDetailViewModel @AssistedInject constructor(
    @Assisted("name") name: String,
    @Assisted("lat") lat: Double,
    @Assisted("lon") lon: Double,
) : ViewModel() {

    @AssistedFactory
    interface LocationDetailViewModelFactory {
        fun create(
            @Assisted("name") name: String,
            @Assisted("lat") lat: Double,
            @Assisted("lon") lon: Double,
        ): LocationDetailViewModel
    }

    private val _state = MutableStateFlow(
        LocationDetailState(
            name = name,
            lat = lat,
            lon = lon,
        )
    )
    val state: StateFlow<LocationDetailState> = _state.asStateFlow()

//    init {
//        viewModelScope.launch {
//            characterId?.let { charId ->
//                getCharacterUsecase.execute(charId)?.let {
//                    _state.value = state.value.copy(
//                        titleToShow = it.name,
//                        imageUrlToShow = it.image,
//                    )
//                }
//            }
//        }
//    }
}