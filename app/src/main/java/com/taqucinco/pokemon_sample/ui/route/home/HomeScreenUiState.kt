package com.taqucinco.pokemon_sample.ui.route.home

import com.taqucinco.pokemon_sample.feature.pokemon.PokemonResource

data class HomeScreenUiState(
    val pokemons: List<PokemonResource> = emptyList(),
    val offset: Int = 0,
    val loading: Boolean = false,
    val remaining: Boolean = true,
    val error: Throwable? = null,
)
