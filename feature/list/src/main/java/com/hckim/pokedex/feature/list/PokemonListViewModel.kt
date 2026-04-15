package com.hckim.pokedex.feature.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hckim.pokedex.core.common.base.MviViewModel
import com.hckim.pokedex.core.domain.usecase.GetFavoriteIdsUseCase
import com.hckim.pokedex.core.domain.usecase.GetPokemonListUseCase
import com.hckim.pokedex.core.domain.usecase.ToggleFavoriteUseCase
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
    private val getPokemonListUseCase: GetPokemonListUseCase,
    private val getFavoriteIdsUseCase: GetFavoriteIdsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
) : ViewModel(), MviViewModel<PokemonListUiState, PokemonListUiIntent, PokemonListUiEffect> {

    private val _uiState = MutableStateFlow(PokemonListUiState())
    override val uiState: StateFlow<PokemonListUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<PokemonListUiEffect>()
    override val effect: SharedFlow<PokemonListUiEffect> = _effect.asSharedFlow()

    private val _searchParams = MutableStateFlow(SearchParams())

    init {
        viewModelScope.launch {
            getFavoriteIdsUseCase().collect { ids ->
                _uiState.update { it.copy(favoriteIds = ids) }
            }
        }
    }

    val pokemonPagingData: Flow<PagingData<Pokemon>> = _searchParams
        .flatMapLatest { params ->
            getPokemonListUseCase(params.query, params.type)
        }
        .cachedIn(viewModelScope)

    override fun onIntent(intent: PokemonListUiIntent) {
        when (intent) {
            is PokemonListUiIntent.Search -> {
                _uiState.update { it.copy(searchQuery = intent.query) }
                _searchParams.update { it.copy(query = intent.query) }
            }

            is PokemonListUiIntent.FilterByType -> {
                _uiState.update { it.copy(selectedType = intent.type) }
                _searchParams.update { it.copy(type = intent.type) }
            }

            is PokemonListUiIntent.Click -> {
                viewModelScope.launch {
                    _effect.emit(PokemonListUiEffect.NavigateToDetail(intent.pokemon.name))
                }
            }

            is PokemonListUiIntent.ToggleFavorite -> {
                viewModelScope.launch {
                    toggleFavoriteUseCase(intent.pokemon)
                }
            }

            PokemonListUiIntent.Refresh -> {
                // Paging 3 handles refresh through the adapter
            }
        }
    }

    private data class SearchParams(
        val query: String = "",
        val type: PokemonType? = null
    )
}
