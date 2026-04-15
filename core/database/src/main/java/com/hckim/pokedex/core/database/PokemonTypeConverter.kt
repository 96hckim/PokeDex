package com.hckim.pokedex.core.database

import androidx.room.TypeConverter
import com.hckim.pokedex.core.model.PokemonType
import kotlinx.serialization.json.Json

class PokemonTypeConverter {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromList(value: List<PokemonType>): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun toList(value: String): List<PokemonType> {
        return try {
            json.decodeFromString(value)
        } catch (e: Exception) {
            emptyList()
        }
    }
}
