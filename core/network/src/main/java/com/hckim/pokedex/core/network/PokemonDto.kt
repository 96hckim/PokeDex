package com.hckim.pokedex.core.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonListResponse(
    @SerialName("count") val count: Int,
    @SerialName("next") val next: String?,
    @SerialName("previous") val previous: String?,
    @SerialName("results") val results: List<NamedApiResource>
)

@Serializable
data class NamedApiResource(
    @SerialName("name") val name: String,
    @SerialName("url") val url: String
)

@Serializable
data class PokemonDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("height") val height: Int,
    @SerialName("weight") val weight: Int,
    @SerialName("sprites") val sprites: PokemonSpritesDto,
    @SerialName("stats") val stats: List<PokemonStatDto>,
    @SerialName("types") val types: List<PokemonTypeDto>,
    @SerialName("abilities") val abilities: List<PokemonAbilityDto>
)

@Serializable
data class PokemonSpritesDto(
    @SerialName("other") val other: OtherSpritesDto? = null
)

@Serializable
data class OtherSpritesDto(
    @SerialName("official-artwork") val officialArtwork: OfficialArtworkDto? = null
)

@Serializable
data class OfficialArtworkDto(
    @SerialName("front_default") val frontDefault: String?
)

@Serializable
data class PokemonStatDto(
    @SerialName("base_stat") val baseStat: Int,
    @SerialName("stat") val stat: NamedApiResource
)

@Serializable
data class PokemonTypeDto(
    @SerialName("slot") val slot: Int,
    @SerialName("type") val type: NamedApiResource
)

@Serializable
data class PokemonAbilityDto(
    @SerialName("ability") val ability: NamedApiResource,
    @SerialName("is_hidden") val isHidden: Boolean,
    @SerialName("slot") val slot: Int
)
