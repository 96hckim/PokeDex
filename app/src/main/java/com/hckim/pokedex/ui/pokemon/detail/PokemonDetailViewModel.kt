package com.hckim.pokedex.ui.pokemon.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hckim.pokedex.domain.repository.PokemonRepository
import com.hckim.pokedex.ui.base.BaseViewModel
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
class PokemonDetailViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel(), BaseViewModel<PokemonDetailViewState, PokemonDetailViewIntent, PokemonDetailViewEffect> {

    private val _uiState = MutableStateFlow(PokemonDetailViewState())
    override val uiState: StateFlow<PokemonDetailViewState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<PokemonDetailViewEffect>()
    override val effect: SharedFlow<PokemonDetailViewEffect> = _effect.asSharedFlow()

    override fun onIntent(intent: PokemonDetailViewIntent) {
        when (intent) {
            is PokemonDetailViewIntent.LoadPokemon -> {
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