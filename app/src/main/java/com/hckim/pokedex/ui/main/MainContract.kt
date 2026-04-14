package com.hckim.pokedex.ui.main

import com.hckim.pokedex.domain.model.Pokemon
import com.hckim.pokedex.ui.base.ViewEffect
import com.hckim.pokedex.ui.base.ViewIntent
import com.hckim.pokedex.ui.base.ViewState

data class MainViewState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val selectedType: String? = null,
    val favoriteIds: List<Int> = emptyList()
) : ViewState

sealed interface MainViewIntent : ViewIntent {
    data class SearchPokemon(val query: String) : MainViewIntent
    data class FilterByType(val type: String?) : MainViewIntent
    data class ClickPokemon(val pokemon: Pokemon) : MainViewIntent
    data class ToggleFavorite(val pokemon: Pokemon) : MainViewIntent
    data object Refresh : MainViewIntent
}

sealed interface MainViewEffect : ViewEffect {
    data class NavigateToDetail(val pokemonName: String) : MainViewEffect
    data class ShowError(val message: String) : MainViewEffect
}