package com.hckim.pokedex.core.domain.usecase

import androidx.paging.PagingData
import com.hckim.pokedex.core.domain.repository.PokemonRepository
import com.hckim.pokedex.core.model.Pokemon
import com.hckim.pokedex.core.model.PokemonType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPokemonListUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    operator fun invoke(query: String = "", type: PokemonType? = null): Flow<PagingData<Pokemon>> {
        return repository.getPokemonList(query, type)
    }
}
