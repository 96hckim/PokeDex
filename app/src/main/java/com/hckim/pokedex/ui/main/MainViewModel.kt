package com.hckim.pokedex.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hckim.pokedex.domain.model.Pokemon
import com.hckim.pokedex.domain.repository.PokemonRepository
import com.hckim.pokedex.ui.base.MviViewModel
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
class MainViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel(), MviViewModel<MainViewState, MainViewIntent, MainViewEffect> {

    private val _uiState = MutableStateFlow(MainViewState())
    override val uiState: StateFlow<MainViewState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<MainViewEffect>()
    override val effect: SharedFlow<MainViewEffect> = _effect.asSharedFlow()

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

    override fun onIntent(intent: MainViewIntent) {
        when (intent) {
            is MainViewIntent.SearchPokemon -> {
                _uiState.update { it.copy(searchQuery = intent.query) }
                _searchParams.update { it.copy(query = intent.query) }
            }

            is MainViewIntent.FilterByType -> {
                _uiState.update { it.copy(selectedType = intent.type) }
                _searchParams.update { it.copy(type = intent.type) }
            }

            is MainViewIntent.ClickPokemon -> {
                viewModelScope.launch {
                    _effect.emit(MainViewEffect.NavigateToDetail(intent.pokemon.name))
                }
            }

            is MainViewIntent.ToggleFavorite -> {
                viewModelScope.launch {
                    repository.toggleFavorite(intent.pokemon)
                }
            }

            MainViewIntent.Refresh -> {
                // Paging 3 handles refresh through the adapter
            }
        }
    }

    private data class SearchParams(
        val query: String = "",
        val type: String? = null
    )
}
