package io.chthonic.weather.domain

import androidx.paging.PagingData
import androidx.paging.map
import io.chthonic.weather.domain.dataapi.RickMortyRepository
import io.chthonic.weather.domain.dataapi.models.CharacterInfo
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

internal class GetCharacterListUseCaseImplTests {

    @MockK
    private lateinit var rickMortyRepository: RickMortyRepository

    private lateinit var getCharacterListUseCase: GetCharacterListUseCaseImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        getCharacterListUseCase = GetCharacterListUseCaseImpl(rickMortyRepository)
    }

    @Test
    fun `given list of CharacterInfo when execute then return mapped PagingData`() = runTest {
        // Given
        val mockCharacter = CharacterInfo(
            id = 1,
            name = "name",
            image = "image",
        )
        val mockPagingData = PagingData.from(listOf(mockCharacter))
        every { rickMortyRepository.getCharacters() } returns flowOf(mockPagingData)

        // When
        val result = getCharacterListUseCase.execute()

        // Then
        result.collect { pagingData ->
            pagingData.map {
                assertEquals(mockCharacter.id, it.id)
                assertEquals(mockCharacter.name, it.name)
                assertEquals(mockCharacter.image, it.image)
            }
        }
    }
}
