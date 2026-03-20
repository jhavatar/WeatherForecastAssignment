package io.chthonic.weather.domain

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.chthonic.weather.domain.presentationapi.GetCharacterListUseCase
import io.chthonic.weather.domain.presentationapi.GetCharacterUseCase
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
}