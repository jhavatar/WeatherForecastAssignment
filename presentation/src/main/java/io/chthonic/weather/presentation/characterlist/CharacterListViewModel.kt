package io.chthonic.weather.presentation.characterlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import io.chthonic.weather.common.models.Outcome
import io.chthonic.weather.domain.presentationapi.GeocodingRepo
import io.chthonic.weather.domain.presentationapi.GetCharacterListUseCase
import io.chthonic.weather.domain.presentationapi.WeatherRepo
import io.chthonic.weather.domain.presentationapi.models.CharacterInfo
import io.chthonic.weather.presentation.wrapper.SideEffect
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CharacterListViewModel @Inject constructor(
    getCharacterListUseCase: GetCharacterListUseCase,
    private val geocodingRepo: GeocodingRepo,
    private val weatherRepo: WeatherRepo,
) : ViewModel() {

    val characterListToDisplay: Flow<PagingData<CharacterInfo>> =
        getCharacterListUseCase.execute()
            .cachedIn(viewModelScope)

    private val _navigateSideEffect = MutableStateFlow<SideEffect<NavigationTarget>?>(null)
    val navigateSideEffect: StateFlow<SideEffect<NavigationTarget>?> =
        _navigateSideEffect.asStateFlow()

    init {
        viewModelScope.launch {
            val searchResult = geocodingRepo.searchCity("London")
            Timber.v("searchCity result = $searchResult")
            if (searchResult is Outcome.Success) {
                val city = searchResult.data[0]
                val weekResult = weatherRepo.getWeekWeatherForecast(lat = city.lat, lon = city.lon)
                Timber.v("week weather result = $weekResult")
                val dayResult = weatherRepo.getCurrentWeatherForecast(lat = city.lat, lon = city.lon)
                Timber.v("current weather result = $dayResult")
            }
        }
    }

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