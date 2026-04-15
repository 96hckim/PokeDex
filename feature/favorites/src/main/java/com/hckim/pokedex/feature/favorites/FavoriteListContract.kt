package com.hckim.pokedex.feature.favorites

import com.hckim.pokedex.core.common.ViewEffect
import com.hckim.pokedex.core.common.ViewIntent
import com.hckim.pokedex.core.common.ViewState
import com.hckim.pokedex.core.model.Pokemon

data class FavoriteListViewState(
    val favoriteIds: List<Int> = emptyList()
) : ViewState

sealed interface FavoriteListViewIntent : ViewIntent {
    data class ClickPokemon(val pokemon: Pokemon) : FavoriteListViewIntent
    data class ToggleFavorite(val pokemon: Pokemon) : FavoriteListViewIntent
}

sealed interface FavoriteListViewEffect : ViewEffect {
    data class NavigateToDetail(val pokemonName: String) : FavoriteListViewEffect
}
