package com.hckim.pokedex.domain.repository

import androidx.paging.PagingData
import com.hckim.pokedex.domain.model.Pokemon
import com.hckim.pokedex.domain.model.PokemonType
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    fun getPokemonList(query: String = "", type: PokemonType? = null): Flow<PagingData<Pokemon>>
    fun getFavoritePokemonList(): Flow<PagingData<Pokemon>>
    suspend fun getPokemonDetails(name: String): Result<Pokemon>
    fun getFavoriteIds(): Flow<List<Int>>
    suspend fun toggleFavorite(pokemon: Pokemon)
    fun isFavorite(id: Int): Flow<Boolean>
}
