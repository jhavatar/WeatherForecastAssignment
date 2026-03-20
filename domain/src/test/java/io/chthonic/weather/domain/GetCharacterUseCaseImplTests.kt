package io.chthonic.weather.domain

import io.chthonic.weather.domain.dataapi.RickMortyRepository
import io.chthonic.weather.domain.dataapi.models.CharacterInfo
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

internal class GetCharacterUseCaseImplTests {

    @MockK
    private lateinit var rickMortyRepository: RickMortyRepository

    private lateinit var getCharacterUseCase: GetCharacterUseCaseImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        getCharacterUseCase = GetCharacterUseCaseImpl(rickMortyRepository)
    }

    @Test
    fun `given valid characterId when execute then should return CharacterInfo`() = runTest {
        // Given
        val characterId = 1
        val mockCharacter = CharacterInfo(
            id = characterId,
            name = "name",
            image = "image",
        )
        every { runBlocking { rickMortyRepository.getCharacter(characterId) } } returns mockCharacter

        // When
        val result = getCharacterUseCase.execute(characterId)

        // Then
        assertNotNull(result)
        assertEquals(result!!.id, characterId)
        assertEquals(result.name, "name")
        assertEquals(result.image, "image")
    }

    @Test
    fun `given invalid characterId when execute then should return null`() = runTest {
        // Given
        val invalidCharacterId = -1
        every { runBlocking { rickMortyRepository.getCharacter(invalidCharacterId) } } returns null

        // When
        val result = getCharacterUseCase.execute(invalidCharacterId)

        // Then
        assertNull(result)
    }
}