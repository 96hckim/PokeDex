package com.hckim.pokedex.core.domain.usecase

import com.hckim.pokedex.core.domain.repository.PokemonRepository
import com.hckim.pokedex.core.model.Pokemon
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ToggleFavoriteUseCaseTest {

    private val repository: PokemonRepository = mockk()
    private lateinit var useCase: ToggleFavoriteUseCase

    @Before
    fun setUp() {
        useCase = ToggleFavoriteUseCase(repository)
    }

    @Test
    fun `invoke는 리포지토리의 toggleFavorite을 호출해야 한다`() = runTest {
        // Given
        val pokemon = Pokemon(
            id = 1,
            name = "bulbasaur",
            imageUrl = "url",
            types = emptyList()
        )
        coEvery { repository.toggleFavorite(pokemon) } returns Unit

        // When
        useCase(pokemon)

        // Then
        coVerify(exactly = 1) { repository.toggleFavorite(pokemon) }
    }
}
