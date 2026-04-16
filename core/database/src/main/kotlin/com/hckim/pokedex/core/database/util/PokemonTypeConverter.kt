package com.hckim.pokedex.core.database.util

import androidx.room.TypeConverter
import com.hckim.pokedex.core.database.model.PokemonStatEntity
import com.hckim.pokedex.core.database.model.PokemonTypeEntity
import kotlinx.serialization.json.Json

internal class PokemonTypeConverter {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromPokemonTypeList(value: List<PokemonTypeEntity>): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun toPokemonTypeList(value: String): List<PokemonTypeEntity> {
        return try {
            json.decodeFromString(value)
        } catch (e: Exception) {
            emptyList()
        }
    }

    @TypeConverter
    fun fromPokemonStatList(value: List<PokemonStatEntity>): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun toPokemonStatList(value: String): List<PokemonStatEntity> {
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