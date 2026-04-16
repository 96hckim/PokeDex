package com.hckim.pokedex.core.database.di

import com.hckim.pokedex.core.database.PokeDatabase
import com.hckim.pokedex.core.database.dao.PokemonDao
import com.hckim.pokedex.core.database.dao.RemoteKeyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object DaoModule {

    @Provides
    fun providesPokemonDao(
        database: PokeDatabase
    ): PokemonDao = database.pokemonDao()

    @Provides
    fun providesRemoteKeyDao(
        database: PokeDatabase
    ): RemoteKeyDao = database.remoteKeyDao()
}
