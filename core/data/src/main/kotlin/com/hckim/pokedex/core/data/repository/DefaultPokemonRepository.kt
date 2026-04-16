package com.hckim.pokedex.core.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.hckim.pokedex.core.data.remotemediator.PokemonRemoteMediator
import com.hckim.pokedex.core.database.PokeDatabase
import com.hckim.pokedex.core.database.model.FavoriteEntity
import com.hckim.pokedex.core.database.model.asExternalModel
import com.hckim.pokedex.core.domain.repository.PokemonRepository
import com.hckim.pokedex.core.model.Pokemon
import com.hckim.pokedex.core.model.PokemonType
import com.hckim.pokedex.core.network.model.asEntity
import com.hckim.pokedex.core.network.retrofit.PokemonApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class DefaultPokemonRepository @Inject constructor(
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
            pagingData.map { it.asExternalModel() }
        }
    }

    override fun getFavoritePokemonList(): Flow<PagingData<Pokemon>> {
        return Pager(
            config = PAGING_CONFIG,
            pagingSourceFactory = { db.pokemonDao().favoritesPagingSource() }
        ).flow.map { pagingData ->
            pagingData.map { it.asExternalModel() }
        }
    }

    override suspend fun getPokemonDetails(name: String): Result<Pokemon> {
        return try {
            val localPokemon = db.pokemonDao().getPokemonByName(name)
            if (localPokemon != null) {
                Result.success(localPokemon.asExternalModel())
            } else {
                val response = api.getPokemonDetails(name)
                val entity = response.asEntity()
                db.pokemonDao().insertAll(listOf(entity))
                Result.success(entity.asExternalModel())
            }
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
