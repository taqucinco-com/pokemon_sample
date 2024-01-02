package com.taqucinco.pokemon_sample.feature.pokemon.pokeBall

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.taqucinco.pokemon_sample.feature.pokemon.PokemonResourceEntity

const val pokeBallTable = "poke_balls"

@Dao
interface PokeBallDao {
    @Query("SELECT * FROM $pokeBallTable")
    fun getAll(): List<PokemonResourceEntity>

    @Insert
    fun insert(entity: PokemonResourceEntity)

    @Delete
    fun delete(entity: PokemonResourceEntity)

    @Query("DELETE FROM $pokeBallTable")
    fun drop()
}