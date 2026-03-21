package io.chthonic.weather.data.geocoding

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.chthonic.weather.domain.dataapi.GeocodingService

@Module
@InstallIn(SingletonComponent::class)
internal class GeocodingSingletonModule {

    @Provides
    fun provideGeocodingService(impl: GeocodingServiceImpl): GeocodingService =
        impl
}