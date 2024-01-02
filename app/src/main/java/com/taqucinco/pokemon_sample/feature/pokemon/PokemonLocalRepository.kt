package com.taqucinco.pokemon_sample.feature.pokemon

interface PokemonLocalRepository {
    val maxCapacity: Int
    suspend fun getPokemonsInPokeBall(): List<PokemonResource>
    suspend fun captureWithPokeBall(pokemon: PokemonResource)
    suspend fun releaseFromPokeBall(name: String)
}
