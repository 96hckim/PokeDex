package com.hckim.pokedex.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hckim.pokedex.domain.model.PokemonType
import kotlinx.serialization.Serializable

@Serializable
data class PokemonTypeEntity(
    val type: PokemonType,
    val slot: Int
)

@Entity(tableName = "pokemon")
data class PokemonEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val imageUrl: String,
    val types: List<PokemonTypeEntity>,
    val height: Int,
    val weight: Int
)

@Entity(tableName = "remote_keys")
data class RemoteKeyEntity(
    @PrimaryKey val pokemonId: Int,
    val prevKey: Int?,
    val nextKey: Int?
)
