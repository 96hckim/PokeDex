package com.hckim.pokedex.ui.pokemon.detail

import com.hckim.pokedex.domain.model.Pokemon
import com.hckim.pokedex.ui.base.ViewEffect
import com.hckim.pokedex.ui.base.ViewIntent
import com.hckim.pokedex.ui.base.ViewState

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
