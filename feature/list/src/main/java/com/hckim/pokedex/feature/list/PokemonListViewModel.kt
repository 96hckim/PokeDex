package com.hckim.pokedex.feature.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hckim.pokedex.core.common.BaseViewModel
import com.hckim.pokedex.core.domain.PokemonRepository
import com.hckim.pokedex.core.model.Pokemon
import com.hckim.pokedex.core.model.PokemonType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel(), BaseViewModel<PokemonListViewState, PokemonListViewIntent, PokemonListViewEffect> {

    private val _uiState = MutableStateFlow(PokemonListViewState())
    override val uiState: StateFlow<PokemonListViewState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<PokemonListViewEffect>()
    override val effect: SharedFlow<PokemonListViewEffect> = _effect.asSharedFlow()

    private val _searchParams = MutableStateFlow(SearchParams())

    init {
        viewModelScope.launch {
            repository.getFavoriteIds().collect { ids ->
                _uiState.update { it.copy(favoriteIds = ids) }
            }
        }
    }

    val pokemonPagingData: Flow<PagingData<Pokemon>> = _searchParams
        .flatMapLatest { params ->
            repository.getPokemonList(params.query, params.type)
        }
        .cachedIn(viewModelScope)

    override fun onIntent(intent: PokemonListViewIntent) {
        when (intent) {
            is PokemonListViewIntent.Search -> {
                _uiState.update { it.copy(searchQuery = intent.query) }
                _searchParams.update { it.copy(query = intent.query) }
            }

            is PokemonListViewIntent.FilterByType -> {
                _uiState.update { it.copy(selectedType = intent.type) }
                _searchParams.update { it.copy(type = intent.type) }
            }

            is PokemonListViewIntent.Click -> {
                viewModelScope.launch {
                    _effect.emit(PokemonListViewEffect.NavigateToDetail(intent.pokemon.name))
                }
            }

            is PokemonListViewIntent.ToggleFavorite -> {
                viewModelScope.launch {
                    repository.toggleFavorite(intent.pokemon)
                }
            }

            PokemonListViewIntent.Refresh -> {
                // Paging 3 handles refresh through the adapter
            }
        }
    }

    private data class SearchParams(
        val query: String = "",
        val type: PokemonType? = null
    )
}
