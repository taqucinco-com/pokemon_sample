package com.taqucinco.pokemon_sample.feature.pokemon

interface PokemonRemoteRepository {
    /**
     * ポケモンの取得可能なリストを取得する
     */
    suspend fun getList(
        limit: Int? = null,
        offset: Int? = null,
    ): PokemonApi.PokemonList.Response

    /**
     * {id}で指定したポケモン詳細を取得する
     * @param id: ID
     */
    suspend fun getPokemon(id: Int): PokemonApi.Pokemon.Response
}