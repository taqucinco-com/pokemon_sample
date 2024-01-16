package com.taqucinco.pokemon_sample.feature.pokemon

import com.taqucinco.pokemon_sample.feature.database.DatabaseFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PokeBallRepositoryImpl @Inject constructor(
    private val dbFactory: DatabaseFactory,
): PokeBallRepository {

    override suspend fun getAll(): List<PokemonResourceEntity> {
        // RoomはMainスレッドからアクセスできないためIOスレッドのスコープで動作させる
        return withContext(Dispatchers.IO) {
            val db = dbFactory.build()
            val dao = db.pokeBallDao()
            val all = dao.getAll()
            db.close()
            return@withContext all
        }
    }

    override suspend fun findBy(name: String): PokemonResourceEntity? {
        return withContext(Dispatchers.IO) {
            val db = dbFactory.build()
            val dao = db.pokeBallDao()
            val all = dao.getAll()
            db.close()

            val entity = all.firstOrNull {
                it.name == name
            }
            return@withContext entity
        }
    }

    override suspend fun insert(pokemon: PokemonResourceEntity) {
        withContext(Dispatchers.IO) {
            val db = dbFactory.build()
            val dao = db.pokeBallDao()
            dao.insert(
                PokemonResourceEntity(
                    name = pokemon.name,
                    url = pokemon.url,
                )
            )
            db.close()
        }
    }

    override suspend fun delete(entity: PokemonResourceEntity) {
        withContext(Dispatchers.IO) {
            val db = dbFactory.build()
            val dao = db.pokeBallDao()
            dao.delete(entity)
            db.close()
        }
    }
}