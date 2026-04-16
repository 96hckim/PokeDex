package com.hckim.pokedex.core.data.di

import com.hckim.pokedex.core.data.repository.DefaultPokemonRepository
import com.hckim.pokedex.core.domain.repository.PokemonRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    internal abstract fun bindsPokemonRepository(
        pokemonRepository: DefaultPokemonRepository
    ): PokemonRepository
}
