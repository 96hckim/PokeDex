package com.hckim.pokedex.core.model

import kotlinx.serialization.Serializable

@Serializable
enum class PokemonType(
    val displayName: String,
    val colorHex: Long
) {
    NORMAL("Normal", 0xFFA8A77A),
    FIRE("Fire", 0xFFEE8130),
    WATER("Water", 0xFF6390F0),
    ELECTRIC("Electric", 0xFFF7D02C),
    GRASS("Grass", 0xFF7AC74C),
    ICE("Ice", 0xFF96D9D6),
    FIGHTING("Fighting", 0xFFC22E28),
    POISON("Poison", 0xFFA33EA1),
    GROUND("Ground", 0xFFE2BF65),
    FLYING("Flying", 0xFFA98FF3),
    PSYCHIC("Psychic", 0xFFF95587),
    BUG("Bug", 0xFFA6B91A),
    ROCK("Rock", 0xFFB6A136),
    GHOST("Ghost", 0xFF735797),
    DRAGON("Dragon", 0xFF6F35FC),
    STEEL("Steel", 0xFFB7B7CE),
    FAIRY("Fairy", 0xFFD685AD),
    UNKNOWN("Unknown", 0xFF888888);

    companion object {
        fun fromString(type: String): PokemonType {
            return entries.find { it.name.equals(type, ignoreCase = true) } ?: UNKNOWN
        }
    }
}
