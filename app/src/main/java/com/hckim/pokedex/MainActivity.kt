package com.hckim.pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hckim.pokedex.ui.detail.DetailScreen
import com.hckim.pokedex.ui.detail.DetailViewModel
import com.hckim.pokedex.ui.main.MainViewModel
import com.hckim.pokedex.ui.main.PokemonGridScreen
import com.hckim.pokedex.ui.theme.PokeDexTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokeDexTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PokeAppNavigation()
                }
            }
        }
    }
}

@Composable
fun PokeAppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            val viewModel: MainViewModel = hiltViewModel()
            PokemonGridScreen(
                viewModel = viewModel,
                onNavigateToDetail = { pokemonName ->
                    navController.navigate("detail/$pokemonName")
                }
            )
        }
        composable(
            route = "detail/{name}",
            arguments = listOf(navArgument("name") { type = NavType.StringType })
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: ""
            val viewModel: DetailViewModel = hiltViewModel()
            DetailScreen(
                viewModel = viewModel,
                pokemonName = name,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
