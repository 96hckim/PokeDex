package com.hckim.pokedex.core.database

import androidx.room.TypeConverter
import com.hckim.pokedex.core.model.PokemonStat
import com.hckim.pokedex.core.model.PokemonType
import kotlinx.serialization.json.Json

class PokemonTypeConverter {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromPokemonTypeList(value: List<PokemonType>): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun toPokemonTypeList(value: String): List<PokemonType> {
        return try {
            json.decodeFromString(value)
        } catch (e: Exception) {
            emptyList()
        }
    }

    @TypeConverter
    fun fromPokemonStatList(value: List<PokemonStat>): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun toPokemonStatList(value: String): List<PokemonStat> {
        return try {
            json.decodeFromString(value)
        } catch (e: Exception) {
            emptyList()
        }
    }

    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        return try {
            json.decodeFromString(value)
        } catch (e: Exception) {
            emptyList()
        }
    }
}
