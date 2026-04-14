package com.hckim.pokedex

import androidx.navigation.NavController
import kotlinx.serialization.Serializable

/**
 * Route definitions using Kotlin Serialization.
 */
sealed interface Route {
    @Serializable
    data object PokemonList : Route

    @Serializable
    data object FavoriteList : Route

    @Serializable
    data class PokemonDetail(val pokemonName: String) : Route
}

/**
 * Centralized navigation actions.
 */
class PokeDexNavigationActions(private val navController: NavController) {
    fun navigateToPokemonDetail(name: String) {
        navController.navigate(Route.PokemonDetail(pokemonName = name))
    }

    fun navigateToFavorites() {
        navController.navigate(Route.FavoriteList)
    }

    fun navigateBack() {
        navController.popBackStack()
    }
}
