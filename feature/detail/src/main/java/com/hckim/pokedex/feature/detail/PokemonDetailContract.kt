package com.hckim.pokedex.feature.detail

import com.hckim.pokedex.core.common.UiEffect
import com.hckim.pokedex.core.common.UiIntent
import com.hckim.pokedex.core.common.UiState
import com.hckim.pokedex.core.model.Pokemon

data class PokemonDetailViewState(
    val pokemon: Pokemon? = null,
    val isLoading: Boolean = false,
    val error: String? = null
) : UiState

sealed interface PokemonDetailViewIntent : UiIntent {
    data class LoadPokemon(val name: String) : PokemonDetailViewIntent
}

sealed interface PokemonDetailViewEffect : UiEffect {
    data object NavigateBack : PokemonDetailViewEffect
}
