package com.hckim.pokedex.ui.pokemon.list

import com.hckim.pokedex.domain.model.Pokemon
import com.hckim.pokedex.domain.model.PokemonType
import com.hckim.pokedex.ui.base.ViewEffect
import com.hckim.pokedex.ui.base.ViewIntent
import com.hckim.pokedex.ui.base.ViewState

data class PokemonListViewState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val selectedType: PokemonType? = null,
    val favoriteIds: List<Int> = emptyList()
) : ViewState

sealed interface PokemonListViewIntent : ViewIntent {
    data class SearchPokemon(val query: String) : PokemonListViewIntent
    data class FilterByType(val type: PokemonType?) : PokemonListViewIntent
    data class ClickPokemon(val pokemon: Pokemon) : PokemonListViewIntent
    data class ToggleFavorite(val pokemon: Pokemon) : PokemonListViewIntent
    data object Refresh : PokemonListViewIntent
}

sealed interface PokemonListViewEffect : ViewEffect {
    data class NavigateToDetail(val pokemonName: String) : PokemonListViewEffect
    data class ShowError(val message: String) : PokemonListViewEffect
}