package com.taqucinco.pokemon_sample.feature.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.taqucinco.pokemon_sample.feature.pokemon.pokeBall.PokeBallDao
import com.taqucinco.pokemon_sample.feature.pokemon.PokemonResourceEntity

@Database(
    entities = [
        PokemonResourceEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
//@TypeConverters(PokemonEntity.Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun pokeBallDao(): PokeBallDao
}
