package com.hckim.pokedex.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hckim.pokedex.core.model.Pokemon

@Entity(tableName = "pokemon")
data class PokemonEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val imageUrl: String,
    val types: List<PokemonTypeEntity>,
    val height: Int,
    val weight: Int,
    val stats: List<PokemonStatEntity>,
    val abilities: List<String>
)

fun PokemonEntity.asExternalModel(): Pokemon {
    return Pokemon(
        id = id,
        name = name,
        imageUrl = imageUrl,
        types = types.map { it.asExternalModel() },
        height = height,
        weight = weight,
        stats = stats.map { it.asExternalModel() },
        abilities = abilities
    )
}
