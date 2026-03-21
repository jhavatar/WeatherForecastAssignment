package io.chthonic.weather.domain

import io.chthonic.weather.common.models.DayForecast
import io.chthonic.weather.common.models.Outcome
import io.chthonic.weather.domain.dataapi.WeatherService
import io.chthonic.weather.domain.presentationapi.WeatherRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepoImpl @Inject constructor(
    private val weatherService: WeatherService,
) : WeatherRepo {
    override suspend fun getCurrentWeatherForecast(lat: Double, lon: Double): Outcome<DayForecast> {
        return weatherService.getCurrentWeather(lat, lon)
    }

    override suspend fun getWeekWeatherForecast(lat: Double, lon: Double): Outcome<List<DayForecast>> {
        return weatherService.getWeeklyWeather(lat, lon)
    }
}