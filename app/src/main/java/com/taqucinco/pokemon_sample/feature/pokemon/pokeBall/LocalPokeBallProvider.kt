package com.taqucinco.pokemon_sample.feature.pokemon.pokeBall

import androidx.compose.runtime.compositionLocalOf

val LocalPokeBallProvider = compositionLocalOf<PokeBallProvidable> {
    // CompositionLocalProviderで後からprovideするので、何もprovideされない場合のerrorを記述する
    error("No LocalCounterStateInterface found!")
}
