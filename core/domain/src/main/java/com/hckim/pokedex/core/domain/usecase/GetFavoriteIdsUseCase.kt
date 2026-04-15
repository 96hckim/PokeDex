package com.hckim.pokedex.core.domain.usecase

import com.hckim.pokedex.core.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteIdsUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    operator fun invoke(): Flow<List<Int>> {
        return repository.getFavoriteIds()
    }
}
