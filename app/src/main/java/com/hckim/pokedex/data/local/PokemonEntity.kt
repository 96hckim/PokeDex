package com.hckim.pokedex.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon")
data class PokemonEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val imageUrl: String,
    val types: String, // Stored as comma-separated string or use TypeConverter
    val height: Int,
    val weight: Int
)

@Entity(tableName = "remote_keys")
data class RemoteKeyEntity(
    @PrimaryKey val pokemonId: Int,
    val prevKey: Int?,
    val nextKey: Int?
)