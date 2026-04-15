package com.hckim.pokedex.core.domain.usecase

import com.hckim.pokedex.core.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsFavoriteUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    operator fun invoke(id: Int): Flow<Boolean> {
        return repository.isFavorite(id)
    }
}
