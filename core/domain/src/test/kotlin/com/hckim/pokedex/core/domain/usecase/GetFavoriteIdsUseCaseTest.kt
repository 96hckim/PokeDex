package com.hckim.pokedex.core.domain.usecase

import app.cash.turbine.test
import com.hckim.pokedex.core.domain.repository.PokemonRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetFavoriteIdsUseCaseTest {

    private val repository: PokemonRepository = mockk()
    private lateinit var useCase: GetFavoriteIdsUseCase

    @Before
    fun setUp() {
        useCase = GetFavoriteIdsUseCase(repository)
    }

    @Test
    fun `invoke는 리포지토리로부터 즐겨찾기 ID 목록 Flow를 반환해야 한다`() = runTest {
        // Given
        val expectedIds = listOf(1, 2, 3)
        every { repository.getFavoriteIds() } returns flowOf(expectedIds)

        // When & Then
        useCase().test {
            assertEquals(expectedIds, awaitItem())
            awaitComplete()
        }
    }
}
