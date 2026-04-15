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
    private val db: PokeDatabase,
    private val remoteMediator: PokemonRemoteMediator
) : PokemonRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getPokemonList(query: String, type: PokemonType?): Flow<PagingData<Pokemon>> {
        val isSearching = query.isNotEmpty() || type != null

        return Pager(
            config = PAGING_CONFIG,
            remoteMediator = if (isSearching) null else remoteMediator,
            pagingSourceFactory = { db.pokemonDao().pagingSource(query, type?.name) }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }

    override fun getFavoritePokemonList(): Flow<PagingData<Pokemon>> {
        return Pager(
            config = PAGING_CONFIG,
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

    companion object {
        private val PAGING_CONFIG = PagingConfig(
            pageSize = 20,
            prefetchDistance = 5,
            initialLoadSize = 40,
            enablePlaceholders = false
        )
    }
}
