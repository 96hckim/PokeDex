package com.hckim.pokedex.feature.detail

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.hckim.pokedex.core.model.PokemonType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailScreen(
    viewModel: PokemonDetailViewModel = hiltViewModel(),
    pokemonName: String,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(pokemonName) {
        viewModel.onIntent(PokemonDetailViewIntent.LoadPokemon(pokemonName))
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
            val mainColor = pokemon.types.firstOrNull()?.colorHex?.let { Color(it) } ?: Color(PokemonType.UNKNOWN.colorHex)

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

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        color = Color.Gray.copy(alpha = 0.05f),
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .padding(16.dp)
                            ) {
                                InfoItem(
                                    label = "Weight",
                                    value = "${pokemon.weight / 10.0} kg",
                                    modifier = Modifier.weight(1f)
                                )
                                Box(
                                    modifier = Modifier
                                        .width(1.dp)
                                        .height(40.dp)
                                        .background(Color.LightGray)
                                )
                                InfoItem(
                                    label = "Height",
                                    value = "${pokemon.height / 10.0} m",
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            if (pokemon.abilities.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(32.dp))
                                Text(
                                    text = "Abilities",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = mainColor
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    pokemon.abilities.forEach { ability ->
                                        AbilityBadge(ability = ability)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(32.dp))
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
fun DetailTypeBadge(type: PokemonType) {
    Surface(
        color = Color(type.colorHex),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        Text(
            text = type.displayName,
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
        animationSpec = tween(durationMillis = 1000),
        label = "stat_anim"
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
            textAlign = TextAlign.End
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

@Composable
fun AbilityBadge(ability: String) {
    Surface(
        color = Color.Gray.copy(alpha = 0.1f),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Text(
            text = ability.replaceFirstChar { it.uppercase() },
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = Color.DarkGray
        )
    }
}
