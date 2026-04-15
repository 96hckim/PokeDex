package com.hckim.pokedex.feature.detail

import com.hckim.pokedex.core.common.ViewEffect
import com.hckim.pokedex.core.common.ViewIntent
import com.hckim.pokedex.core.common.ViewState
import com.hckim.pokedex.core.model.Pokemon

data class PokemonDetailViewState(
    val pokemon: Pokemon? = null,
    val isLoading: Boolean = false,
    val error: String? = null
) : ViewState

sealed interface PokemonDetailViewIntent : ViewIntent {
    data class LoadPokemon(val name: String) : PokemonDetailViewIntent
}

sealed interface PokemonDetailViewEffect : ViewEffect {
    data object NavigateBack : PokemonDetailViewEffect
}
