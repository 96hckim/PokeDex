package com.hckim.pokedex.core.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.hckim.pokedex.core.database.PokeDatabase
import com.hckim.pokedex.core.database.model.FavoriteEntity
import com.hckim.pokedex.core.database.model.PokemonEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class PokemonDaoTest {

    private lateinit var db: PokeDatabase
    private lateinit var dao: PokemonDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, PokeDatabase::class.java).build()
        dao = db.pokemonDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun `pokemon_데이터_삽입_및_조회_테스트`() = runTest {
        // Given
        val pokemon = PokemonEntity(
            id = 1,
            name = "bulbasaur",
            imageUrl = "url",
            types = emptyList(),
            height = 7,
            weight = 69,
            stats = emptyList(),
            abilities = emptyList()
        )
        dao.insertAll(listOf(pokemon))

        // When
        val result = dao.getPokemonById(1)

        // Then
        assertEquals(pokemon.name, result?.name)
    }

    @Test
    fun `즐겨찾기_추가_및_삭제_테스트`() = runTest {
        // Given
        val id = 1
        val favorite = FavoriteEntity(id = id, name = "bulbasaur")

        // When: 추가
        dao.insertFavorite(favorite)

        // Then: 추가 확인
        assertTrue(dao.isFavorite(id).first())
        assertEquals(listOf(id), dao.getFavoriteIds().first())

        // When: 삭제
        dao.deleteFavorite(id)

        // Then: 삭제 확인
        assertFalse(dao.isFavorite(id).first())
        assertTrue(dao.getFavoriteIds().first().isEmpty())
    }
}
