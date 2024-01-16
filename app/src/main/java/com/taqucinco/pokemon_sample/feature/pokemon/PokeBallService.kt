package com.taqucinco.pokemon_sample.feature.pokemon

import com.taqucinco.pokemon_sample.error.PokemonError
import javax.inject.Inject

class PokeBallService @Inject constructor(
    private val repo: PokeBallRepository
): PokeBallServable {

    private val pokeBallLimit = 3
    override val maxCapacity: Int = pokeBallLimit

    override suspend fun getPokemonsInPokeBall(): List<PokemonResource> {
        return repo.getAll()
    }

    override suspend fun captureWithPokeBall(pokemon: PokemonResource) {
        val all = repo.getAll()
        if (all.size >= pokeBallLimit) {
            throw PokemonError.PokeBallFullFilled
        }
        val entity = PokemonResourceEntity(
            name = pokemon.name,
            url = pokemon.url,
        )
        repo.insert(entity)
    }

    override suspend fun releaseFromPokeBall(name: String) {
        val entity = repo.findBy(name)
        if (entity != null) {
            repo.delete(entity)
        } else {
            throw PokemonError.NotCapturedWithPokeBall
        }
    }
}