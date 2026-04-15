package com.hckim.pokedex.feature.detail

import com.hckim.pokedex.core.common.UiEffect
import com.hckim.pokedex.core.common.UiIntent
import com.hckim.pokedex.core.common.UiState
import com.hckim.pokedex.core.model.Pokemon

data class PokemonDetailUiState(
    val pokemon: Pokemon? = null,
    val isLoading: Boolean = false,
    val error: String? = null
) : UiState

sealed interface PokemonDetailUiIntent : UiIntent {
    data class LoadPokemon(val name: String) : PokemonDetailUiIntent
}

sealed interface PokemonDetailUiEffect : UiEffect {
    data object NavigateBack : PokemonDetailUiEffect
}
