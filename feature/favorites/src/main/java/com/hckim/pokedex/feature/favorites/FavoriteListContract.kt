package com.hckim.pokedex.feature.favorites

import com.hckim.pokedex.core.common.UiEffect
import com.hckim.pokedex.core.common.UiIntent
import com.hckim.pokedex.core.common.UiState
import com.hckim.pokedex.core.model.Pokemon

data class FavoriteListUiState(
    val favoriteIds: List<Int> = emptyList()
) : UiState

sealed interface FavoriteListUiIntent : UiIntent {
    data class ClickPokemon(val pokemon: Pokemon) : FavoriteListUiIntent
    data class ToggleFavorite(val pokemon: Pokemon) : FavoriteListUiIntent
}

sealed interface FavoriteListUiEffect : UiEffect {
    data class NavigateToDetail(val pokemonName: String) : FavoriteListUiEffect
}
