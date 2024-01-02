package com.taqucinco.pokemon_sample.feature.pokemon

interface PokemonResource {
    val name: String
    val url: String

    fun extractId(): Int? {
        // "https://pokeapi.co/api/v2/pokemon/33/"の最後の数字がIDになっているのでそれを取り出す
        val pattern = """^https:\/\/(?:[a-zA-Z0-9-]+\.)?pokeapi\.co\/api\/v\d+\/pokemon\/(\d+)"""
        val regex = pattern.toRegex()
        val matched = regex.find(url)
        val subGroup = matched?.groupValues?.last()
        return subGroup?.toInt()
    }
}

interface Pokemon {
    val id: Int
    val name: String
    val sprites: PokemonSprites

    interface PokemonSprites {
        val frontDefault: String
    }
}
