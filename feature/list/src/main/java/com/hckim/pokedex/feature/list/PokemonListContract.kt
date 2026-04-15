package com.hckim.pokedex.feature.list

import com.hckim.pokedex.core.common.UiEffect
import com.hckim.pokedex.core.common.UiIntent
import com.hckim.pokedex.core.common.UiState
import com.hckim.pokedex.core.model.Pokemon
import com.hckim.pokedex.core.model.PokemonType

data class PokemonListViewState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val selectedType: PokemonType? = null,
    val favoriteIds: List<Int> = emptyList()
) : UiState

sealed interface PokemonListViewIntent : UiIntent {
    data class Search(val query: String) : PokemonListViewIntent
    data class FilterByType(val type: PokemonType?) : PokemonListViewIntent
    data class Click(val pokemon: Pokemon) : PokemonListViewIntent
    data class ToggleFavorite(val pokemon: Pokemon) : PokemonListViewIntent
    data object Refresh : PokemonListViewIntent
}

sealed interface PokemonListViewEffect : UiEffect {
    data class NavigateToDetail(val pokemonName: String) : PokemonListViewEffect
    data class ShowError(val message: String) : PokemonListViewEffect
}
