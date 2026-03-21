package io.chthonic.data.weather

import io.chthonic.data.weather.models.CurrentWeatherResponse
import io.chthonic.data.weather.models.WeeklyWeatherResponse
import io.chthonic.weather.common.models.CurrentForecast
import io.chthonic.weather.common.models.DayForecast
import io.chthonic.weather.common.models.Outcome
import io.chthonic.weather.data.common.rest.executeAsResult
import io.chthonic.weather.domain.dataapi.WeatherService
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject

private const val OPEN_METEO_END_POINT = "https://api.open-meteo.com/v1/forecast"

class WeatherServiceImpl @Inject constructor(private val httpClient: HttpClient) : WeatherService {

    override suspend fun getCurrentWeather(lat: Double, lon: Double): Outcome<CurrentForecast> {
        val response: Outcome<CurrentWeatherResponse> = httpClient.executeAsResult {
            get(OPEN_METEO_END_POINT) {
                parameter("latitude", lat)
                parameter("longitude", lon)
                parameter(
                    "current",
                    "temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m,is_day",
                )
                // auto: use the timezone of the requested coordinates
                parameter("timezone", "auto")
            }
        }
        return response.map { it.current.toDomainModel() }
    }

    override suspend fun getWeeklyWeather(lat: Double, lon: Double): Outcome<List<DayForecast>> {
        val response: Outcome<WeeklyWeatherResponse> = httpClient.executeAsResult {
            get(OPEN_METEO_END_POINT) {
                parameter("latitude", lat)
                parameter("longitude", lon)
                parameter(
                    "daily",
                    "weather_code,temperature_2m_max,temperature_2m_min,precipitation_probability_max",
                )
                // auto: use the timezone of the requested coordinates
                parameter("timezone", "auto")
                parameter("forecast_days", 7)
            }
        }
        return response.map { it.daily.toDomainModel() }
    }
}