package com.hckim.pokedex.ui.pokemon.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hckim.pokedex.domain.model.Pokemon
import com.hckim.pokedex.domain.repository.PokemonRepository
import com.hckim.pokedex.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteListViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel(), BaseViewModel<FavoriteListViewState, FavoriteListViewIntent, FavoriteListViewEffect> {

    private val _uiState = MutableStateFlow(FavoriteListViewState())
    override val uiState: StateFlow<FavoriteListViewState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<FavoriteListViewEffect>()
    override val effect: SharedFlow<FavoriteListViewEffect> = _effect.asSharedFlow()

    init {
        viewModelScope.launch {
            repository.getFavoriteIds().collect { ids ->
                _uiState.update { it.copy(favoriteIds = ids) }
            }
        }
    }

    val favoritePokemonPagingData: Flow<PagingData<Pokemon>> = repository
        .getFavoritePokemonList()
        .cachedIn(viewModelScope)

    override fun onIntent(intent: FavoriteListViewIntent) {
        when (intent) {
            is FavoriteListViewIntent.ClickPokemon -> {
                viewModelScope.launch {
                    _effect.emit(FavoriteListViewEffect.NavigateToDetail(intent.pokemon.name))
                }
            }

            is FavoriteListViewIntent.ToggleFavorite -> {
                viewModelScope.launch {
                    repository.toggleFavorite(intent.pokemon)
                }
            }
        }
    }
}
