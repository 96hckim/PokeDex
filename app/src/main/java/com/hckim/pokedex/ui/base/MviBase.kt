package com.hckim.pokedex.ui.base

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface ViewState

interface ViewIntent

interface ViewEffect

interface MviViewModel<S : ViewState, I : ViewIntent, E : ViewEffect> {
    val uiState: StateFlow<S>
    val effect: SharedFlow<E>
    fun onIntent(intent: I)
}