package com.hckim.pokedex.core.data.repository

import app.cash.turbine.test
import com.hckim.pokedex.core.data.remotemediator.PokemonRemoteMediator
import com.hckim.pokedex.core.database.PokeDatabase
import com.hckim.pokedex.core.database.dao.PokemonDao
import com.hckim.pokedex.core.model.Pokemon
import com.hckim.pokedex.core.network.retrofit.PokemonApi
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DefaultPokemonRepositoryTest {

    private val api: PokemonApi = mockk()
    private val db: PokeDatabase = mockk()
    private val dao: PokemonDao = mockk()
    private val remoteMediator: PokemonRemoteMediator = mockk()
    private lateinit var repository: DefaultPokemonRepository

    @Before
    fun setUp() {
        every { db.pokemonDao() } returns dao
        repository = DefaultPokemonRepository(api, db, remoteMediator)
    }

    @Test
    fun `toggleFavorite은 이미 즐겨찾기인 경우 삭제를 호출해야 한다`() = runTest {
        // Given
        val pokemon = Pokemon(id = 1, name = "bulbasaur", imageUrl = "", types = emptyList())
        every { dao.isFavorite(pokemon.id) } returns flowOf(true)
        coEvery { dao.deleteFavorite(pokemon.id) } returns Unit

        // When
        repository.toggleFavorite(pokemon)

        // Then
        coVerify { dao.deleteFavorite(pokemon.id) }
    }

    @Test
    fun `toggleFavorite은 즐겨찾기가 아닌 경우 삽입을 호출해야 한다`() = runTest {
        // Given
        val pokemon = Pokemon(id = 1, name = "bulbasaur", imageUrl = "", types = emptyList())
        every { dao.isFavorite(pokemon.id) } returns flowOf(false)
        coEvery { dao.insertFavorite(any()) } returns Unit

        // When
        repository.toggleFavorite(pokemon)

        // Then
        coVerify { dao.insertFavorite(match { it.id == pokemon.id && it.name == pokemon.name }) }
    }

    @Test
    fun `isFavorite은 DAO의 결과를 반환해야 한다`() = runTest {
        // Given
        val id = 1
        every { dao.isFavorite(id) } returns flowOf(true)

        // When & Then
        repository.isFavorite(id).test {
            assertEquals(true, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `getFavoriteIds는 DAO의 결과를 반환해야 한다`() = runTest {
        // Given
        val ids = listOf(1, 2, 3)
        every { dao.getFavoriteIds() } returns flowOf(ids)

        // When & Then
        repository.getFavoriteIds().test {
            assertEquals(ids, awaitItem())
            awaitComplete()
        }
    }
}
