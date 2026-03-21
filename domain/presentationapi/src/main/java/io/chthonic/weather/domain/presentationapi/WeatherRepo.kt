package io.chthonic.weather.domain.presentationapi

import io.chthonic.weather.common.models.CurrentForecast
import io.chthonic.weather.common.models.DayForecast
import io.chthonic.weather.common.models.Outcome

interface WeatherRepo {
    suspend fun getCurrentWeatherForecast(lat: Double, lon: Double): Outcome<CurrentForecast>

    suspend fun getWeekWeatherForecast(lat: Double, lon: Double): Outcome<List<DayForecast>>
}