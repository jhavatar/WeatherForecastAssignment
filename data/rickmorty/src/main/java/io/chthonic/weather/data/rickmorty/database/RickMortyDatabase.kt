package io.chthonic.weather.data.rickmorty.database

import androidx.room.Database
import androidx.room.RoomDatabase
import io.chthonic.weather.data.rickmorty.database.models.CharacterInfoDb

private const val CURRENT_DATABASE_VERSION = 1

@Database(
    entities = [
        CharacterInfoDb::class,
    ],
    version = CURRENT_DATABASE_VERSION
)
abstract class RickMortyDatabase : RoomDatabase() {
    abstract fun charactersDao(): CharactersDao
}