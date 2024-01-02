package com.taqucinco.pokemon_sample.feature.pokemon.pokeBall

import androidx.compose.runtime.Composable
import com.taqucinco.pokemon_sample.feature.pokemon.PokemonResource
import kotlinx.coroutines.flow.StateFlow

interface PokeBallProvidable {
    @Composable fun capturedByPokeBall(): List<PokemonResource>
    val state: StateFlow<List<PokemonResource>>
    suspend fun toggle(pokemonResource: PokemonResource)
    val maxCapacity: Int
}
