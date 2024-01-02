package com.taqucinco.pokemon_sample.ui.hooks.pokemon

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.taqucinco.pokemon_sample.error.PokemonError
import com.taqucinco.pokemon_sample.feature.pokemon.Pokemon
import com.taqucinco.pokemon_sample.feature.pokemon.PokemonRemoteRepository
import com.taqucinco.pokemon_sample.feature.pokemon.PokemonResource
import com.taqucinco.pokemon_sample.utility.AsyncResult
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@Stable
data class PokemonsState(
    val list: List<AsyncResult<Pokemon>>,
    val reload: () -> Unit,
    val getLoaded: () -> List<Pokemon> = {
        list.mapNotNull {
            when (it) {
                is AsyncResult.Success -> it.data
                else -> null
            }
        }
    },
    val loading: () -> Boolean = {
        list.any { it is AsyncResult.Loading }
    },
    val hasError: () -> Boolean = {
        list.any { it is AsyncResult.Error }
    },
) {
}

@Composable
fun rememberPokemonsState(
    resourcesFlow: StateFlow<List<PokemonResource>>,
    remoteRepo: PokemonRemoteRepository,
): PokemonsState {
    val scope = rememberCoroutineScope()
    var pokemons by remember { mutableStateOf<List<AsyncResult<Pokemon>>>(emptyList()) }
    val resources = resourcesFlow.collectAsStateWithLifecycle().value

    val prepare: () -> Unit = {
        pokemons = resources
            .filter { it.extractId() != null }
            .map { AsyncResult.Loading }
    }

    val fetch: () -> Unit = {
        scope.launch {
            val identifiers = resources.mapNotNull { it.extractId() }
            // 各々のasync関数のエラーを捕捉するためにrunCatchingにしてResult<Response>に変換する
            val deferred = identifiers.map {
                async {
                    kotlin.runCatching {
                        remoteRepo.getPokemon(it)
                    }
                }
            }
            val responses = deferred.map { it.await() }
            val mapped = responses.map {
                try {
                    val value = it.getOrThrow()
                    return@map AsyncResult.Success(value)
                } catch (t: Throwable) {
                    return@map AsyncResult.Error(PokemonError.FetchingFail)
                }
            }
            pokemons = mapped
        }
    }

    LaunchedEffect(resources) {
        prepare()
        fetch()
    }

    return remember(pokemons) {
        PokemonsState(
            list = pokemons,
            reload = {
                prepare()
                fetch()
            }
        )
    }
}

@Composable
fun fetchPokemonState(
    id: Int,
    remoteRepo: PokemonRemoteRepository
): State<AsyncResult<Pokemon>> {
    return produceState<AsyncResult<Pokemon>>(initialValue = AsyncResult.Loading) {
        value = try {
            val pokemon = remoteRepo.getPokemon(id)
            AsyncResult.Success(pokemon)
        } catch (t: Throwable) {
            AsyncResult.Error(t)
        }
    }
}
