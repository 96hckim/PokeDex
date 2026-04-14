package com.hckim.pokedex

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.hckim.pokedex.ui.pokemon.detail.PokemonDetailScreen
import com.hckim.pokedex.ui.pokemon.favorites.FavoriteListScreen
import com.hckim.pokedex.ui.pokemon.list.PokemonListScreen

@Composable
fun PokeDexNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: Route = Route.PokemonList,
    navActions: PokeDexNavigationActions = remember(navController) {
        PokeDexNavigationActions(navController)
    }
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
        enterTransition = { slideInHorizontally { it } + fadeIn() },
        exitTransition = { slideOutHorizontally { -it } + fadeOut() },
        popEnterTransition = { slideInHorizontally { -it } + fadeIn() },
        popExitTransition = { slideOutHorizontally { it } + fadeOut() }
    ) {
        composable<Route.PokemonList> {
            PokemonListScreen(
                onNavigateToDetail = { name -> navActions.navigateToPokemonDetail(name) },
                onNavigateToFavorites = { navActions.navigateToFavorites() }
            )
        }

        composable<Route.FavoriteList> {
            FavoriteListScreen(
                onNavigateToDetail = { name -> navActions.navigateToPokemonDetail(name) },
                onNavigateBack = { navActions.navigateBack() }
            )
        }

        composable<Route.PokemonDetail> { backStackEntry ->
            val detail: Route.PokemonDetail = backStackEntry.toRoute()
            PokemonDetailScreen(
                pokemonName = detail.pokemonName,
                onNavigateBack = { navActions.navigateBack() }
            )
        }
    }
}