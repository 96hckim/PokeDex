package com.hckim.pokedex.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [PokemonEntity::class, RemoteKeyEntity::class, FavoriteEntity::class],
    version = 2,
    exportSchema = false
)
abstract class PokeDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
    abstract fun remoteKeyDao(): RemoteKeyDao
}