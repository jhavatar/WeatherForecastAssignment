package io.chthonic.weather.domain.dataapi

import io.chthonic.weather.common.models.CurrentForecast
import io.chthonic.weather.common.models.DayForecast
import io.chthonic.weather.common.models.Outcome

interface WeatherService {

    suspend fun getCurrentWeather(lat: Double, lon: Double): Outcome<CurrentForecast>

    suspend fun getWeeklyWeather(lat: Double, lon: Double): Outcome<List<DayForecast>>
}