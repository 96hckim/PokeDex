package com.hckim.pokedex.feature.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hckim.pokedex.core.common.base.MviViewModel
import com.hckim.pokedex.core.domain.usecase.GetPokemonDetailsUseCase
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
    private val getPokemonDetailsUseCase: GetPokemonDetailsUseCase
) : ViewModel(), MviViewModel<PokemonDetailUiState, PokemonDetailUiIntent, PokemonDetailUiEffect> {

    private val _uiState = MutableStateFlow(PokemonDetailUiState())
    override val uiState: StateFlow<PokemonDetailUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<PokemonDetailUiEffect>()
    override val effect: SharedFlow<PokemonDetailUiEffect> = _effect.asSharedFlow()

    override fun onIntent(intent: PokemonDetailUiIntent) {
        when (intent) {
            is PokemonDetailUiIntent.LoadPokemon -> {
                loadPokemon(intent.name)
            }
        }
    }

    private fun loadPokemon(name: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getPokemonDetailsUseCase(name)
                .onSuccess { pokemon ->
                    _uiState.update { it.copy(pokemon = pokemon, isLoading = false) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(error = error.message, isLoading = false) }
                }
        }
    }
}
