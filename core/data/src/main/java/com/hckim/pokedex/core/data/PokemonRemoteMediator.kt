package com.hckim.pokedex.core.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.hckim.pokedex.core.database.PokeDatabase
import com.hckim.pokedex.core.database.PokemonEntity
import com.hckim.pokedex.core.database.RemoteKeyEntity
import com.hckim.pokedex.core.network.PokemonApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

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
        // 1. 진입점 확인 (현재 어떤 작업을 하려는지)
        Log.d("RemoteMediator", "🚀 [START] LoadType: $loadType, ConfigPageSize: ${state.config.pageSize}")

        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyAtClosestToCurrentPosition(state)
                val currentOffset = remoteKeys?.nextKey?.minus(state.config.pageSize) ?: 0
                Log.d("RemoteMediator", "🔄 REFRESH: Offset -> $currentOffset")
                currentOffset
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null
                ).also { Log.d("RemoteMediator", "⏹ PREPEND Skip: End of reached") }
                Log.d("RemoteMediator", "⬆️ PREPEND: PrevKey -> $prevKey")
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null
                ).also { Log.d("RemoteMediator", "⏹ APPEND Skip: End of reached") }
                Log.d("RemoteMediator", "⬇️ APPEND: NextKey -> $nextKey")
                nextKey
            }
        }

        try {
            // 2. 네트워크 요청 직전/직후 (API는 살아있는가?)
            Log.d("RemoteMediator", "🌐 Fetching API... Offset: $page, Limit: ${state.config.pageSize}")
            val response = api.getPokemonList(offset = page, limit = state.config.pageSize)
            Log.d("RemoteMediator", "✅ API List Success: Total count in response: ${response.results.size}")

            val endOfPaginationReached = response.next == null

            // 3. 병렬 요청 확인 (상세 정보를 잘 받아오는가?)
            val entities = coroutineScope {
                Log.d("RemoteMediator", "🧪 Fetching Details in parallel for ${response.results.size} items")
                response.results.map { resource ->
                    async { api.getPokemonDetails(resource.name).toEntity() }
                }.awaitAll()
            }
            Log.d("RemoteMediator", "✅ All Details Fetched: Size -> ${entities.size}")

            // 4. DB 트랜잭션 (데이터 증발의 주범 확인)
            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    Log.d("RemoteMediator", "🧹 [REFRESH] Clearing all data...")
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
                db.pokemonDao().insertAll(entities)
                Log.d("RemoteMediator", "💾 DB Insert Completed. Keys: ${keys.size}, Entities: ${entities.size}")
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            // 5. 예외 상황 (어디서 터졌는가?)
            Log.e("RemoteMediator", "❌ [ERROR] Load failed!", exception)
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
