package com.hckim.pokedex.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hckim.pokedex.core.database.dao.PokemonDao
import com.hckim.pokedex.core.database.dao.RemoteKeyDao
import com.hckim.pokedex.core.database.model.FavoriteEntity
import com.hckim.pokedex.core.database.model.PokemonEntity
import com.hckim.pokedex.core.database.model.RemoteKeyEntity
import com.hckim.pokedex.core.database.util.PokemonTypeConverter

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
