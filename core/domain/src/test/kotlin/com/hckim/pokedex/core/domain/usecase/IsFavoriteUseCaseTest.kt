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

class IsFavoriteUseCaseTest {

    private val repository: PokemonRepository = mockk()
    private lateinit var useCase: IsFavoriteUseCase

    @Before
    fun setUp() {
        useCase = IsFavoriteUseCase(repository)
    }

    @Test
    fun `invoke는 리포지토리로부터 즐겨찾기 여부 Flow를 반환해야 한다`() = runTest {
        // Given
        val id = 1
        every { repository.isFavorite(id) } returns flowOf(true)

        // When & Then
        useCase(id).test {
            assertEquals(true, awaitItem())
            awaitComplete()
        }
    }
}
