package com.taqucinco.pokemon_sample.error

sealed class PokemonError: Error() {
    object PokeBallFullFilled: PokemonError()
    object NotCapturedWithPokeBall: PokemonError()
    object FetchingFail: PokemonError()
}