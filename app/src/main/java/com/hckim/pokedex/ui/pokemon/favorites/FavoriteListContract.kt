package com.hckim.pokedex.ui.pokemon.favorites

import com.hckim.pokedex.domain.model.Pokemon
import com.hckim.pokedex.ui.base.ViewEffect
import com.hckim.pokedex.ui.base.ViewIntent
import com.hckim.pokedex.ui.base.ViewState

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
