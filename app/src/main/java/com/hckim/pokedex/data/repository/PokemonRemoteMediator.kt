package com.hckim.pokedex.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.hckim.pokedex.data.local.PokeDatabase
import com.hckim.pokedex.data.local.PokemonEntity
import com.hckim.pokedex.data.local.RemoteKeyEntity
import com.hckim.pokedex.data.remote.PokemonApi
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class PokemonRemoteMediator(
    private val api: PokemonApi,
    private val db: PokeDatabase
) : RemoteMediator<Int, PokemonEntity>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PokemonEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyAtClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(state.config.pageSize) ?: 0
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null
                )
                prevKey
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null
                )
                nextKey
            }
        }

        try {
            val response = api.getPokemonList(
                offset = page,
                limit = state.config.pageSize
            )

            val endOfPaginationReached = response.next == null

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    db.remoteKeyDao().clearRemoteKeys()
                    db.pokemonDao().clearAll()
                }

                val prevKey = if (page == 0) null else page - state.config.pageSize
                val nextKey = if (endOfPaginationReached) null else page + state.config.pageSize

                val keys = response.results.map {
                    val id = it.url.split("/").dropLast(1).last().toInt()
                    RemoteKeyEntity(pokemonId = id, prevKey = prevKey, nextKey = nextKey)
                }
                db.remoteKeyDao().insertAll(keys)

                // We need details to get the image and types for the list
                val entities = response.results.map { resource ->
                    val details = api.getPokemonDetails(resource.name)
                    details.toEntity()
                }
                db.pokemonDao().insertAll(entities)
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, PokemonEntity>): RemoteKeyEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { pokemon ->
                db.remoteKeyDao().remoteKeysPokemonId(pokemon.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, PokemonEntity>): RemoteKeyEntity? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { pokemon ->
                db.remoteKeyDao().remoteKeysPokemonId(pokemon.id)
            }
    }

    private suspend fun getRemoteKeyAtClosestToCurrentPosition(state: PagingState<Int, PokemonEntity>): RemoteKeyEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                db.remoteKeyDao().remoteKeysPokemonId(id)
            }
        }
    }
}