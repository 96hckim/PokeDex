package com.hckim.pokedex.core.database.model

import com.hckim.pokedex.core.model.PokemonStat
import kotlinx.serialization.Serializable

@Serializable
data class PokemonStatEntity(
    val name: String,
    val baseStat: Int
)

fun PokemonStatEntity.asExternalModel(): PokemonStat = PokemonStat(
    name = name,
    baseStat = baseStat
)
