package com.taqucinco.pokemon_sample.feature.pokemon

interface PokeBallRepository {
    suspend fun getAll(): List<PokemonResource>
    suspend fun findBy(name: String): PokemonResourceEntity?
    suspend fun insert(pokemon: PokemonResourceEntity)
    suspend fun delete(entity: PokemonResourceEntity)
}
