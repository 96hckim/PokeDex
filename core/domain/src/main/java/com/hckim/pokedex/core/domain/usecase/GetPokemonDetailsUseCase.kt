package com.hckim.pokedex.core.domain.usecase

import com.hckim.pokedex.core.domain.repository.PokemonRepository
import com.hckim.pokedex.core.model.Pokemon
import javax.inject.Inject

class GetPokemonDetailsUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(name: String): Result<Pokemon> {
        return repository.getPokemonDetails(name)
    }
}
