package com.hckim.pokedex.core.database.di

import android.content.Context
import androidx.room.Room
import com.hckim.pokedex.core.database.PokeDatabase
import com.hckim.pokedex.core.database.PokemonDao
import com.hckim.pokedex.core.database.RemoteKeyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PokeDatabase {
        return Room.databaseBuilder(
            context,
            PokeDatabase::class.java,
            "poke_db"
        ).fallbackToDestructiveMigration(false).build()
    }

    @Provides
    fun providePokemonDao(database: PokeDatabase): PokemonDao = database.pokemonDao()

    @Provides
    fun provideRemoteKeyDao(database: PokeDatabase): RemoteKeyDao = database.remoteKeyDao()
}
