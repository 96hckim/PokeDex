package com.hckim.pokedex.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.hckim.pokedex.data.local.FavoriteEntity
import com.hckim.pokedex.data.local.PokeDatabase
import com.hckim.pokedex.data.remote.PokemonApi
import com.hckim.pokedex.domain.model.Pokemon
import com.hckim.pokedex.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val api: PokemonApi,
    private val db: PokeDatabase
) : PokemonRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getPokemonList(query: String, type: String?): Flow<PagingData<Pokemon>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 2,
                enablePlaceholders = false
            ),
            remoteMediator = PokemonRemoteMediator(api, db),
            pagingSourceFactory = { db.pokemonDao().pagingSource(query, type) }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }

    override suspend fun getPokemonDetails(name: String): Result<Pokemon> {
        return try {
            val response = api.getPokemonDetails(name)
            Result.success(response.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getFavoriteIds(): Flow<List<Int>> {
        return db.pokemonDao().getFavoriteIds()
    }

    override suspend fun toggleFavorite(pokemon: Pokemon) {
        val isFav = db.pokemonDao().isFavorite(pokemon.id).first()
        if (isFav) {
            db.pokemonDao().deleteFavorite(pokemon.id)
        } else {
            db.pokemonDao().insertFavorite(FavoriteEntity(pokemon.id, pokemon.name))
        }
    }

    override fun isFavorite(id: Int): Flow<Boolean> {
        return db.pokemonDao().isFavorite(id)
    }
}
