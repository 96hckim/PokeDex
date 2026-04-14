package com.hckim.pokedex.ui.detail

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.hckim.pokedex.domain.model.Pokemon
import com.hckim.pokedex.ui.theme.getPokemonTypeColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    viewModel: DetailViewModel,
    pokemonName: String,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(pokemonName) {
        viewModel.onIntent(DetailViewIntent.LoadPokemon(pokemonName))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = pokemonName.replaceFirstChar { it.uppercase() }) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        uiState.pokemon?.let { pokemon ->
            val mainColor = getPokemonTypeColor(pokemon.types.firstOrNull() ?: "normal")
            
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(mainColor)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = pokemon.imageUrl,
                            contentDescription = pokemon.name,
                            modifier = Modifier.size(200.dp)
                        )
                    }

                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(24.dp)
                                .fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                pokemon.types.forEach { type ->
                                    DetailTypeBadge(type = type)
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))
                            
                            Text(
                                text = "Base Stats",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = mainColor
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            pokemon.stats.forEach { stat ->
                                StatBar(
                                    statName = stat.name,
                                    statValue = stat.baseStat,
                                    color = mainColor
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            Row(modifier = Modifier.fillMaxWidth()) {
                                InfoItem(label = "Weight", value = "${pokemon.weight / 10.0} kg", modifier = Modifier.weight(1f))
                                InfoItem(label = "Height", value = "${pokemon.height / 10.0} m", modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        } ?: run {
            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
fun DetailTypeBadge(type: String) {
    val color = getPokemonTypeColor(type)
    Surface(
        color = color,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        Text(
            text = type.replaceFirstChar { it.uppercase() },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelLarge,
            color = Color.White
        )
    }
}

@Composable
fun StatBar(statName: String, statValue: Int, color: Color) {
    val animatedValue by animateFloatAsState(
        targetValue = statValue / 255f,
        animationSpec = tween(durationMillis = 1000)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = statName.uppercase(),
            modifier = Modifier.width(60.dp),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = statValue.toString(),
            modifier = Modifier.width(35.dp),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = androidx.compose.ui.text.style.TextAlign.End
        )
        Spacer(modifier = Modifier.width(8.dp))
        LinearProgressIndicator(
            progress = { animatedValue },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .background(Color.LightGray, RoundedCornerShape(4.dp)),
            color = color,
            trackColor = Color.Transparent
        )
    }
}

@Composable
fun InfoItem(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
    }
}
