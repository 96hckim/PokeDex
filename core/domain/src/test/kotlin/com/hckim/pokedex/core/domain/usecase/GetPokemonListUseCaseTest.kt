package com.hckim.pokedex.core.domain.usecase

import androidx.paging.PagingData
import app.cash.turbine.test
import com.hckim.pokedex.core.domain.repository.PokemonRepository
import com.hckim.pokedex.core.model.Pokemon
import com.hckim.pokedex.core.model.PokemonType
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class GetPokemonListUseCaseTest {

    private val repository: PokemonRepository = mockk()
    private lateinit var useCase: GetPokemonListUseCase

    @Before
    fun setUp() {
        useCase = GetPokemonListUseCase(repository)
    }

    @Test
    fun `invoke는 리포지토리로부터 페이징 데이터 Flow를 반환해야 한다`() = runTest {
        // Given
        val query = "bulba"
        val type = PokemonType.GRASS
        val pagingData = PagingData.from(listOf<Pokemon>())
        every { repository.getPokemonList(query, type) } returns flowOf(pagingData)

        // When & Then
        useCase(query, type).test {
            assertNotNull(awaitItem())
            awaitComplete()
        }
    }
}
