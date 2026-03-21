package io.chthonic.data.weather

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.chthonic.weather.domain.dataapi.WeatherService

@Module
@InstallIn(SingletonComponent::class)
internal class WeatherSingletonModule {

    @Provides
    fun provideWeatherService(impl: WeatherServiceImpl): WeatherService =
        impl
}