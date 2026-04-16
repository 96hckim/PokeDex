package com.hckim.pokedex.core.database.model

import com.hckim.pokedex.core.model.PokemonType
import kotlinx.serialization.Serializable

@Serializable
data class PokemonTypeEntity(
    val type: String
)

fun PokemonTypeEntity.asExternalModel(): PokemonType = PokemonType.fromString(type)
