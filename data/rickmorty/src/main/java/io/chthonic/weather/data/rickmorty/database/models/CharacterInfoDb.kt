package io.chthonic.weather.data.rickmorty.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

const val CHARACTER_TABLE_NAME = "character_table"

@Entity(tableName = CHARACTER_TABLE_NAME)
data class CharacterInfoDb(
    @PrimaryKey
    val id: Int,
    val name: String,
    val image: String,
)