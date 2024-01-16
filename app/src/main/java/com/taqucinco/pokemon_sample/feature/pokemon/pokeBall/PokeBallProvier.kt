package com.taqucinco.pokemon_sample.feature.pokemon.pokeBall

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import com.taqucinco.pokemon_sample.feature.analytics.FirebaseAnalyticsEvent
import com.taqucinco.pokemon_sample.feature.analytics.FirebaseAnalyticsServable
import com.taqucinco.pokemon_sample.feature.pokemon.PokemonResource
import com.taqucinco.pokemon_sample.feature.pokemon.PokeBallServable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class PokeBallProvider @Inject constructor(
    private val service: PokeBallServable,
    private val fa: FirebaseAnalyticsServable,
): PokeBallProvidable, CoroutineScope by CoroutineScope(Dispatchers.IO)
{
    private val _state = MutableStateFlow(emptyList<PokemonResource>())
    override val state: StateFlow<List<PokemonResource>> = _state.asStateFlow()

    override val maxCapacity: Int = service.maxCapacity

    /**
     * ポケモンの捕獲と解放をトグル操作する
     */
    override suspend fun toggle(pokemonResource: PokemonResource) {
        val value = _state.value
        val matched = value.filter { it.name == pokemonResource.name && it.url == pokemonResource.url }
        if (matched.isEmpty()) {
            service.captureWithPokeBall(pokemonResource)
            fa.logEvent(FirebaseAnalyticsEvent.PokeBall(pokemonName = pokemonResource.name, captured = true).generated)
            async(Dispatchers.Main) { _state.value = listOf(*(value).toTypedArray(), pokemonResource) }.await()
        } else {
            service.releaseFromPokeBall(pokemonResource.name)
            fa.logEvent(FirebaseAnalyticsEvent.PokeBall(pokemonName = pokemonResource.name, captured = false).generated)
            async(Dispatchers.Main) { _state.value = value.filterNot { it in matched } }.await()
        }
    }

    /**
     * モンスターボールの中にいるポケモンのFlowを監視可能なComposableの状態に変換する
     */
    @SuppressLint("StateFlowValueCalledInComposition")
    @Composable
    override fun capturedByPokeBall(): List<PokemonResource> {
        return produceState<List<PokemonResource>>(initialValue = _state.value) {
            _state.value = service.getPokemonsInPokeBall()
            _state.stateIn(
                scope = GlobalScope,
                started = SharingStarted.Eagerly,
                initialValue = emptyList()
            )
                .collect { value = it }
        }.value
    }
}
