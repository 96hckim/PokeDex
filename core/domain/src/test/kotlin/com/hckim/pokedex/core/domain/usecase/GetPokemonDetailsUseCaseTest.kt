package com.hckim.pokedex.core.domain.usecase

import com.hckim.pokedex.core.domain.repository.PokemonRepository
import com.hckim.pokedex.core.model.Pokemon
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetPokemonDetailsUseCaseTest {

    private val repository: PokemonRepository = mockk()
    private lateinit var useCase: GetPokemonDetailsUseCase

    @Before
    fun setUp() {
        useCase = GetPokemonDetailsUseCase(repository)
    }

    @Test
    fun `invoke는 리포지토리로부터 포켓몬 상세 정보를 반환해야 한다`() = runTest {
        // Given
        val name = "bulbasaur"
        val pokemon = Pokemon(
            id = 1,
            name = name,
            imageUrl = "url",
            types = emptyList()
        )
        val expected = Result.success(pokemon)
        coEvery { repository.getPokemonDetails(name) } returns expected

        // When
        val result = useCase(name)

        // Then
        assertEquals(expected, result)
    }
}
