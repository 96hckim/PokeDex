package com.hckim.pokedex.feature.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hckim.pokedex.core.common.base.MviViewModel
import com.hckim.pokedex.core.domain.PokemonRepository
import com.hckim.pokedex.core.model.Pokemon
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
) : ViewModel(), MviViewModel<FavoriteListUiState, FavoriteListUiIntent, FavoriteListUiEffect> {

    private val _uiState = MutableStateFlow(FavoriteListUiState())
    override val uiState: StateFlow<FavoriteListUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<FavoriteListUiEffect>()
    override val effect: SharedFlow<FavoriteListUiEffect> = _effect.asSharedFlow()

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

    override fun onIntent(intent: FavoriteListUiIntent) {
        when (intent) {
            is FavoriteListUiIntent.ClickPokemon -> {
                viewModelScope.launch {
                    _effect.emit(FavoriteListUiEffect.NavigateToDetail(intent.pokemon.name))
                }
            }

            is FavoriteListUiIntent.ToggleFavorite -> {
                viewModelScope.launch {
                    repository.toggleFavorite(intent.pokemon)
                }
            }
        }
    }
}
