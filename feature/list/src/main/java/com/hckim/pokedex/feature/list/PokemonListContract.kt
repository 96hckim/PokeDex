package com.hckim.pokedex.feature.list

import com.hckim.pokedex.core.common.base.UiEffect
import com.hckim.pokedex.core.common.base.UiIntent
import com.hckim.pokedex.core.common.base.UiState
import com.hckim.pokedex.core.model.Pokemon
import com.hckim.pokedex.core.model.PokemonType

data class PokemonListUiState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val selectedType: PokemonType? = null,
    val favoriteIds: List<Int> = emptyList()
) : UiState

sealed interface PokemonListUiIntent : UiIntent {
    data class Search(val query: String) : PokemonListUiIntent
    data class FilterByType(val type: PokemonType?) : PokemonListUiIntent
    data class Click(val pokemon: Pokemon) : PokemonListUiIntent
    data class ToggleFavorite(val pokemon: Pokemon) : PokemonListUiIntent
    data object Refresh : PokemonListUiIntent
}

sealed interface PokemonListUiEffect : UiEffect {
    data class NavigateToDetail(val pokemonName: String) : PokemonListUiEffect
    data class ShowError(val message: String) : PokemonListUiEffect
}
