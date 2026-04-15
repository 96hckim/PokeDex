package com.hckim.pokedex.core.domain.usecase

import androidx.paging.PagingData
import com.hckim.pokedex.core.domain.repository.PokemonRepository
import com.hckim.pokedex.core.model.Pokemon
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoritePokemonListUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    operator fun invoke(): Flow<PagingData<Pokemon>> {
        return repository.getFavoritePokemonList()
    }
}
