package io.chthonic.weather.data.location

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.chthonic.weather.domain.dataapi.LocationService

@Module
@InstallIn(SingletonComponent::class)
internal class LocationSingletonModule {

    @Provides
    fun provideLocationService(impl: LocationServiceImpl): LocationService = impl
}
