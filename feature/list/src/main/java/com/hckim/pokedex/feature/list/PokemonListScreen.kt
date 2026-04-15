package com.hckim.pokedex.feature.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.hckim.pokedex.core.model.PokemonType
import com.hckim.pokedex.core.ui.components.PokemonCard
import com.hckim.pokedex.core.ui.components.ShimmerPokemonItem
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen(
    viewModel: PokemonListViewModel = hiltViewModel(),
    onNavigateToDetail: (String) -> Unit,
    onNavigateToFavorites: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pokemonItems = viewModel.pokemonPagingData.collectAsLazyPagingItems()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is PokemonListViewEffect.NavigateToDetail -> onNavigateToDetail(effect.pokemonName)
                is PokemonListViewEffect.ShowError -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer)) {
                TopAppBar(
                    title = { Text("Pokédex", fontWeight = FontWeight.Bold) },
                    actions = {
                        IconButton(onClick = onNavigateToFavorites) {
                            Icon(Icons.Default.Favorite, contentDescription = "Favorites")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
                SearchBar(
                    query = uiState.searchQuery,
                    onQueryChange = { viewModel.onIntent(PokemonListViewIntent.Search(it)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
                TypeFilterRow(
                    selectedType = uiState.selectedType,
                    onTypeSelected = { viewModel.onIntent(PokemonListViewIntent.FilterByType(it)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (pokemonItems.loadState.refresh is LoadState.Loading) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(10, key = { "shimmer_$it" }) {
                        ShimmerPokemonItem()
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        count = pokemonItems.itemCount,
                        key = pokemonItems.itemKey { it.id },
                        contentType = pokemonItems.itemContentType { "pokemon" }
                    ) { index ->
                        pokemonItems[index]?.let { pokemon ->
                            PokemonCard(
                                pokemon = pokemon,
                                isFavorite = uiState.favoriteIds.contains(pokemon.id),
                                onClick = { viewModel.onIntent(PokemonListViewIntent.Click(pokemon)) },
                                onFavoriteClick = { viewModel.onIntent(PokemonListViewIntent.ToggleFavorite(pokemon)) }
                            )
                        }
                    }

                    if (pokemonItems.loadState.append is LoadState.Loading) {
                        item(key = "loading_indicator") { LoadingIndicator() }
                    }
                }
            }
        }
    }
}

@Composable
fun TypeFilterRow(
    selectedType: PokemonType?,
    onTypeSelected: (PokemonType?) -> Unit,
    modifier: Modifier = Modifier
) {
    val types = remember { PokemonType.entries.filter { it != PokemonType.UNKNOWN } }

    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected = selectedType == null,
                onClick = { onTypeSelected(null) },
                label = { Text("All") }
            )
        }
        items(types, key = { it.name }) { type ->
            FilterChip(
                selected = selectedType == type,
                onClick = { onTypeSelected(if (selectedType == type) null else type) },
                label = { Text(type.displayName) }
            )
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier,
        placeholder = { Text("Search Pokemon...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        shape = RoundedCornerShape(24.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White,
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
        )
    )
}

@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
