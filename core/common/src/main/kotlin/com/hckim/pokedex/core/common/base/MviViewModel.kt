package com.hckim.pokedex.core.common.base

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface UiState

interface UiIntent

interface UiEffect

interface MviViewModel<S : UiState, I : UiIntent, E : UiEffect> {
    val uiState: StateFlow<S>
    val effect: SharedFlow<E>
    fun onIntent(intent: I)
}
