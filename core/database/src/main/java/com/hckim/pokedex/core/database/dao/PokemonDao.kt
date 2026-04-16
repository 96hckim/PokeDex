package com.hckim.pokedex.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hckim.pokedex.core.database.model.FavoriteEntity
import com.hckim.pokedex.core.database.model.PokemonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pokemon: List<PokemonEntity>)

    @Query(
        """
    SELECT * FROM pokemon 
    WHERE name LIKE '%' || :query || '%' 
    AND (:type IS NULL OR types LIKE '%' || :type || '%')
    ORDER BY id ASC
"""
    )
    fun pagingSource(query: String, type: String?): PagingSource<Int, PokemonEntity>

    @Query(
        """
    SELECT * FROM pokemon 
    WHERE id IN (SELECT id FROM favorites)
    ORDER BY id ASC
"""
    )
    fun favoritesPagingSource(): PagingSource<Int, PokemonEntity>

    @Query("DELETE FROM pokemon")
    suspend fun clearAll()

    @Query("SELECT * FROM pokemon WHERE id = :id")
    suspend fun getPokemonById(id: Int): PokemonEntity?

    @Query("SELECT * FROM pokemon WHERE name = :name")
    suspend fun getPokemonByName(name: String): PokemonEntity?

    @Query("SELECT id FROM favorites")
    fun getFavoriteIds(): Flow<List<Int>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE id = :id")
    suspend fun deleteFavorite(id: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE id = :id)")
    fun isFavorite(id: Int): Flow<Boolean>
}
