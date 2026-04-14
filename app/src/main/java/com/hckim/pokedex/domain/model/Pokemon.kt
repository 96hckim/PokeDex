package com.hckim.pokedex.domain.model

data class Pokemon(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val types: List<String>,
    val height: Int = 0,
    val weight: Int = 0,
    val stats: List<PokemonStat> = emptyList(),
    val abilities: List<String> = emptyList()
)

data class PokemonStat(
    val name: String,
    val baseStat: Int
)