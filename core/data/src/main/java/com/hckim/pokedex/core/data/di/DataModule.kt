package com.hckim.pokedex.core.data.di

import com.hckim.pokedex.core.data.PokemonRepositoryImpl
import com.hckim.pokedex.core.domain.PokemonRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindPokemonRepository(
        pokemonRepositoryImpl: PokemonRepositoryImpl
    ): PokemonRepository
}
