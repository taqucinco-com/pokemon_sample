package com.taqucinco.pokemon_sample.feature.pokemon

import com.taqucinco.pokemon_sample.error.PokemonError
import com.taqucinco.pokemon_sample.feature.database.DatabaseFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PokemonLocalRepositoryImpl @Inject constructor(
    private val dbFactory: DatabaseFactory,
): PokemonLocalRepository {

    private val pokeBallLimit = 3
    override val maxCapacity: Int = pokeBallLimit

    override suspend fun getPokemonsInPokeBall(): List<PokemonResource> {
        // RoomはMainスレッドからアクセスできないためIOスレッドのスコープで動作させる
        return withContext(Dispatchers.IO) {
            val db = dbFactory.build()
            val dao = db.pokeBallDao()
            val all = dao.getAll()
            db.close()
            return@withContext all
        }
    }

    override suspend fun captureWithPokeBall(pokemon: PokemonResource) {
        withContext(Dispatchers.IO) {
            val db = dbFactory.build()
            val dao = db.pokeBallDao()
            val all = dao.getAll()
            if (all.size >= pokeBallLimit) {
                db.close()
                throw PokemonError.PokeBallFullFilled
            }

            dao.insert(
                PokemonResourceEntity(
                    name = pokemon.name,
                    url = pokemon.url,
                )
            )
            db.close()
        }
    }

    override suspend fun releaseFromPokeBall(name: String) {
        withContext(Dispatchers.IO) {
            val db = dbFactory.build()
            val dao = db.pokeBallDao()

            val pokemons = dao.getAll()
            val entity = pokemons.firstOrNull {
                it.name == name
            }

            if (entity != null) {
                dao.delete(entity)
                db.close()
            } else {
                throw PokemonError.NotCapturedWithPokeBall
            }
        }
    }
}