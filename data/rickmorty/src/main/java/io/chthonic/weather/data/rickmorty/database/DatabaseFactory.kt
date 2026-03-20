package io.chthonic.weather.data.rickmorty.database

import android.content.Context
import androidx.room.Room

private const val DB_NAME = "RickMortyDatabase"

object DatabaseFactory {
    fun createDatabase(context: Context): RickMortyDatabase {
        return Room
            .databaseBuilder(context, RickMortyDatabase::class.java, DB_NAME)
            .build()
    }
}