package com.taqucinco.pokemon_sample.feature.pokemon

import com.taqucinco.pokemon_sample.feature.http.rest.RestClientInterface
import kotlinx.serialization.json.Json
import javax.inject.Inject

class PokemonRemoteRepositoryImpl @Inject constructor(
    private val restClient: RestClientInterface
): PokemonRemoteRepository {
    override suspend fun getList(
        limit: Int?,
        offset: Int?,
    ): PokemonApi.PokemonList.Response {
        val req = PokemonApi.PokemonList.Request(limit = limit, offset = offset)
        val response = restClient.get(
            endpoint = PokemonApi.PokemonList.endpoint,
            queryParams = req.queryParameter,
            headers = mapOf("Accept" to "application/json")
        )
        val jsonString = String(response.data, Charsets.UTF_8)
        return Json { ignoreUnknownKeys = true }.decodeFromString<PokemonApi.PokemonList.Response>(jsonString)
    }

    override suspend fun getPokemon(id: Int): PokemonApi.Pokemon.Response {
        val req = PokemonApi.Pokemon.Request(id = id)
        val response = restClient.get(
            endpoint = req.url(),
            headers = mapOf("Accept" to "application/json")
        )
        val jsonString = String(response.data, Charsets.UTF_8)
        return Json { ignoreUnknownKeys = true }.decodeFromString<PokemonApi.Pokemon.Response>(jsonString)
    }
}