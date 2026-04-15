package com.hckim.pokedex.feature.favorites

import com.hckim.pokedex.core.common.UiEffect
import com.hckim.pokedex.core.common.UiIntent
import com.hckim.pokedex.core.common.UiState
import com.hckim.pokedex.core.model.Pokemon

data class FavoriteListViewState(
    val favoriteIds: List<Int> = emptyList()
) : UiState

sealed interface FavoriteListViewIntent : UiIntent {
    data class ClickPokemon(val pokemon: Pokemon) : FavoriteListViewIntent
    data class ToggleFavorite(val pokemon: Pokemon) : FavoriteListViewIntent
}

sealed interface FavoriteListViewEffect : UiEffect {
    data class NavigateToDetail(val pokemonName: String) : FavoriteListViewEffect
}
