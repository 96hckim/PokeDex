package com.hckim.pokedex.feature.list

import app.cash.turbine.test
import com.hckim.pokedex.core.domain.usecase.GetFavoriteIdsUseCase
import com.hckim.pokedex.core.domain.usecase.GetPokemonListUseCase
import com.hckim.pokedex.core.domain.usecase.ToggleFavoriteUseCase
import com.hckim.pokedex.core.model.Pokemon
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PokemonListViewModelTest {

    private val getPokemonListUseCase: GetPokemonListUseCase = mockk()
    private val getFavoriteIdsUseCase: GetFavoriteIdsUseCase = mockk()
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase = mockk()

    private lateinit var viewModel: PokemonListViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { getFavoriteIdsUseCase() } returns flowOf(emptyList())
        viewModel = PokemonListViewModel(
            getPokemonListUseCase,
            getFavoriteIdsUseCase,
            toggleFavoriteUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `초기화 시 즐겨찾기 ID 목록을 가져와야 한다`() = runTest {
        // Given
        val favoriteIds = listOf(1, 2, 3)
        every { getFavoriteIdsUseCase() } returns flowOf(favoriteIds)

        // When
        // ViewModel을 다시 생성하여 init 블록 실행
        val vm = PokemonListViewModel(getPokemonListUseCase, getFavoriteIdsUseCase, toggleFavoriteUseCase)

        // Then
        vm.uiState.test {
            assertEquals(favoriteIds, awaitItem().favoriteIds)
        }
    }

    @Test
    fun `Search 인텐트 전달 시 검색어가 업데이트되어야 한다`() = runTest {
        // Given
        val query = "pikachu"

        // When
        viewModel.onIntent(PokemonListUiIntent.Search(query))

        // Then
        viewModel.uiState.test {
            assertEquals(query, awaitItem().searchQuery)
        }
    }

    @Test
    fun `ToggleFavorite 인텐트 전달 시 유스케이스를 호출해야 한다`() = runTest {
        // Given
        val pokemon = Pokemon(id = 1, name = "bulbasaur", imageUrl = "", types = emptyList())
        coEvery { toggleFavoriteUseCase(pokemon) } returns Unit

        // When
        viewModel.onIntent(PokemonListUiIntent.ToggleFavorite(pokemon))

        // Then
        coVerify { toggleFavoriteUseCase(pokemon) }
    }

    @Test
    fun `Click 인텐트 전달 시 상세 화면 이동 이펙트를 발생시켜야 한다`() = runTest {
        // Given
        val pokemon = Pokemon(id = 1, name = "bulbasaur", imageUrl = "", types = emptyList())

        // When & Then
        viewModel.effect.test {
            viewModel.onIntent(PokemonListUiIntent.Click(pokemon))
            val effect = awaitItem()
            assert(effect is PokemonListUiEffect.NavigateToDetail && effect.pokemonName == pokemon.name)
        }
    }
}
