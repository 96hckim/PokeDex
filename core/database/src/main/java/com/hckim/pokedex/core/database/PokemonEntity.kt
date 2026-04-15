package com.hckim.pokedex.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hckim.pokedex.core.model.PokemonType

@Entity(tableName = "pokemon")
data class PokemonEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val imageUrl: String,
    val types: List<PokemonType>,
    val height: Int,
    val weight: Int
)

@Entity(tableName = "remote_keys")
data class RemoteKeyEntity(
    @PrimaryKey val pokemonId: Int,
    val prevKey: Int?,
    val nextKey: Int?
)
