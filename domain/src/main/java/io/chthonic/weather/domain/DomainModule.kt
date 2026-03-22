package io.chthonic.weather.domain

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.chthonic.weather.domain.presentationapi.GeocodingRepo
import io.chthonic.weather.domain.presentationapi.GetCharacterListUseCase
import io.chthonic.weather.domain.presentationapi.GetCharacterUseCase
import io.chthonic.weather.domain.presentationapi.LocationRepo
import io.chthonic.weather.domain.presentationapi.WeatherRepo
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class DomainModule {

    @Provides
    @Singleton
    fun provideGetCharacterListUseCase(impl: GetCharacterListUseCaseImpl): GetCharacterListUseCase =
        impl

    @Provides
    @Singleton
    fun provideGetCharacterUseCase(impl: GetCharacterUseCaseImpl): GetCharacterUseCase = impl

    @Provides
    @Singleton
    fun provideGeocodingRepo(impl: GeocodingRepoImpl): GeocodingRepo =
        impl

    @Provides
    @Singleton
    fun provideWeatherRepo(impl: WeatherRepoImpl): WeatherRepo =
        impl

    @Provides
    @Singleton
    fun provideLocationRepo(impl: LocationRepoImpl): LocationRepo =
        impl
}