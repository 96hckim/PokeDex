package com.hckim.pokedex.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hckim.pokedex.domain.repository.PokemonRepository
import com.hckim.pokedex.ui.base.MviViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
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
class DetailViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel(), MviViewModel<DetailViewState, DetailViewIntent, DetailViewEffect> {

    private val _uiState = MutableStateFlow(DetailViewState())
    override val uiState: StateFlow<DetailViewState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<DetailViewEffect>()
    override val effect: SharedFlow<DetailViewEffect> = _effect.asSharedFlow()

    override fun onIntent(intent: DetailViewIntent) {
        when (intent) {
            is DetailViewIntent.LoadPokemon -> {
                loadPokemon(intent.name)
            }
        }
    }

    private fun loadPokemon(name: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.getPokemonDetails(name)
                .onSuccess { pokemon ->
                    _uiState.update { it.copy(pokemon = pokemon, isLoading = false) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(error = error.message, isLoading = false) }
                }
        }
    }
}