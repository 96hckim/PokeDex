package com.hckim.pokedex.ui.detail

import com.hckim.pokedex.domain.model.Pokemon
import com.hckim.pokedex.ui.base.ViewEffect
import com.hckim.pokedex.ui.base.ViewIntent
import com.hckim.pokedex.ui.base.ViewState

data class DetailViewState(
    val pokemon: Pokemon? = null,
    val isLoading: Boolean = false,
    val error: String? = null
) : ViewState

sealed interface DetailViewIntent : ViewIntent {
    data class LoadPokemon(val name: String) : DetailViewIntent
}

sealed interface DetailViewEffect : ViewEffect {
    data object NavigateBack : DetailViewEffect
}