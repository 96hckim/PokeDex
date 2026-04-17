package com.hckim.pokedex.feature.detail

import app.cash.turbine.test
import com.hckim.pokedex.core.domain.usecase.GetPokemonDetailsUseCase
import com.hckim.pokedex.core.model.Pokemon
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PokemonDetailViewModelTest {

    private val getPokemonDetailsUseCase: GetPokemonDetailsUseCase = mockk()
    private lateinit var viewModel: PokemonDetailViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = PokemonDetailViewModel(getPokemonDetailsUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `LoadPokemon 인텐트 전달 시 성공하면 포켓몬 상세 정보를 업데이트해야 한다`() = runTest {
        // Given
        val name = "bulbasaur"
        val pokemon = Pokemon(id = 1, name = name, imageUrl = "", types = emptyList())
        coEvery { getPokemonDetailsUseCase(name) } returns Result.success(pokemon)

        // When
        viewModel.onIntent(PokemonDetailUiIntent.LoadPokemon(name))

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(pokemon, state.pokemon)
            assertEquals(false, state.isLoading)
        }
    }

    @Test
    fun `LoadPokemon 인텐트 전달 시 실패하면 에러 메시지를 업데이트해야 한다`() = runTest {
        // Given
        val name = "unknown"
        val errorMessage = "Not Found"
        coEvery { getPokemonDetailsUseCase(name) } returns Result.failure(Exception(errorMessage))

        // When
        viewModel.onIntent(PokemonDetailUiIntent.LoadPokemon(name))

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(errorMessage, state.error)
            assertEquals(false, state.isLoading)
        }
    }
}
