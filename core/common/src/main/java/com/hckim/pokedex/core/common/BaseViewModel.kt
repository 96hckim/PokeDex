package com.hckim.pokedex.core.common

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface ViewState

interface ViewIntent

interface ViewEffect

interface BaseViewModel<S : ViewState, I : ViewIntent, E : ViewEffect> {
    val uiState: StateFlow<S>
    val effect: SharedFlow<E>
    fun onIntent(intent: I)
}
