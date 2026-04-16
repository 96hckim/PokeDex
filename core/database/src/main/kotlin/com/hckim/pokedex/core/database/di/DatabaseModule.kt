package com.hckim.pokedex.core.database.di

import android.content.Context
import androidx.room.Room
import com.hckim.pokedex.core.database.PokeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    @Provides
    @Singleton
    fun providesDatabase(
        @ApplicationContext context: Context
    ): PokeDatabase = Room.databaseBuilder(
        context,
        PokeDatabase::class.java,
        "poke_db"
    ).fallbackToDestructiveMigration(true).build()
}
