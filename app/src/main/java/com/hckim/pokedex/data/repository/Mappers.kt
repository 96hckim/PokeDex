package com.hckim.pokedex.data.repository

import com.hckim.pokedex.data.local.PokemonEntity
import com.hckim.pokedex.data.local.PokemonTypeEntity
import com.hckim.pokedex.data.remote.PokemonDto
import com.hckim.pokedex.domain.model.Pokemon
import com.hckim.pokedex.domain.model.PokemonStat
import com.hckim.pokedex.domain.model.PokemonType

fun PokemonDto.toEntity(): PokemonEntity {
    return PokemonEntity(
        id = id,
        name = name,
        imageUrl = sprites.other?.officialArtwork?.frontDefault ?: "",
        types = types.map { PokemonTypeEntity(PokemonType.fromString(it.type.name), it.slot) },
        height = height,
        weight = weight
    )
}

fun PokemonEntity.toDomain(): Pokemon {
    return Pokemon(
        id = id,
        name = name,
        imageUrl = imageUrl,
        types = types.sortedBy { it.slot }.map { it.type },
        height = height,
        weight = weight
    )
}

fun PokemonDto.toDomain(): Pokemon {
    return Pokemon(
        id = id,
        name = name,
        imageUrl = sprites.other?.officialArtwork?.frontDefault ?: "",
        types = types.sortedBy { it.slot }.map { PokemonType.fromString(it.type.name) },
        height = height,
        weight = weight,
        stats = stats.map { PokemonStat(it.stat.name, it.baseStat) },
        abilities = abilities.map { it.ability.name }
    )
}
