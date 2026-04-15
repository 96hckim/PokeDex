package com.hckim.pokedex.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        PokemonEntity::class,
        RemoteKeyEntity::class,
        FavoriteEntity::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(PokemonTypeConverter::class)
abstract class PokeDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
    abstract fun remoteKeyDao(): RemoteKeyDao
}
