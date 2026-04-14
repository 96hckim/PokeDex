package com.hckim.pokedex.data.local

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

class PokemonTypeConverter {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromList(value: List<PokemonTypeEntity>): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun toList(value: String): List<PokemonTypeEntity> {
        return try {
            json.decodeFromString(value)
        } catch (e: Exception) {
            emptyList()
        }
    }
}
