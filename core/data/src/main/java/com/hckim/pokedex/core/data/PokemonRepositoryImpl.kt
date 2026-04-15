package com.hckim.pokedex.core.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.hckim.pokedex.core.database.FavoriteEntity
import com.hckim.pokedex.core.database.PokeDatabase
import com.hckim.pokedex.core.domain.PokemonRepository
import com.hckim.pokedex.core.model.Pokemon
import com.hckim.pokedex.core.model.PokemonType
import com.hckim.pokedex.core.network.PokemonApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val api: PokemonApi,
    private val db: PokeDatabase
) : PokemonRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getPokemonList(query: String, type: PokemonType?): Flow<PagingData<Pokemon>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 5,
                enablePlaceholders = false,
                initialLoadSize = 40
            ),
            remoteMediator = PokemonRemoteMediator(api, db),
            pagingSourceFactory = { db.pokemonDao().pagingSource(query, type?.name) }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }

    override fun getFavoritePokemonList(): Flow<PagingData<Pokemon>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { db.pokemonDao().favoritesPagingSource() }
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
