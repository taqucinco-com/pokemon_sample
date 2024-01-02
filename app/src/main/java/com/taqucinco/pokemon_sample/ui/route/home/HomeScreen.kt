package com.taqucinco.pokemon_sample.ui.route.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.taqucinco.pokemon_sample.error.PokemonError
import com.taqucinco.pokemon_sample.feature.pokemon.pokeBall.LocalPokeBallProvider
import com.taqucinco.pokemon_sample.ui.component.TopAppBar
import com.taqucinco.pokemon_sample.ui.component.screen.ErrorScreen
import com.taqucinco.pokemon_sample.ui.hooks.AppState
import com.taqucinco.pokemon_sample.ui.route.home._component.ListedPokemon
import com.taqucinco.pokemon_sample.ui.route.home._component.PokemonList as PokemonScrollList
import com.taqucinco.pokemon_sample.ui.route.home._component.PokemonListSkeleton
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/// ホーム画面はGoogle公式ドキュメントに近いViewModelを用いた方法で実装
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    scaffoldPadding: PaddingValues? = null,
    appState: AppState,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val scope = rememberCoroutineScope()
    val localPokeBall = LocalPokeBallProvider.current
    val capturedByPokeBall = localPokeBall.capturedByPokeBall()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pokemons = uiState.pokemons.map { pokemon ->
        return@map object : ListedPokemon {
            override val name = pokemon.name
            override val url = pokemon.url
            override val captured = capturedByPokeBall.any { pokemon.name == it.name }
        }
    }

    LaunchedEffect(Unit) {
        scope.launch {
            delay(1500) // demoとしてスケルトンスクリーンのshimmer effectを見せるためだけに遅らせる
            viewModel.fetchPokemon()
        }
    }

    val handleError: (t: Throwable) -> Unit = {
        when (it) {
            PokemonError.PokeBallFullFilled -> {
                scope.launch {
                    appState.showSnackBar("モンスターボールへの捕獲は${localPokeBall.maxCapacity}匹までです。", null)
                }
            }
            else -> {
                when (it) {
                    is CancellationException -> { /* アニメーションの最中などにタブ移動すると発生するが無視して良い */ }
                    else -> { viewModel.setError(it) }
                }
            }
        }
    }

    @Composable fun PokemonList() {
        PokemonScrollList(
            pokemons,
            onScrollThreshold = { viewModel.fetchPokemon() },
            loading = uiState.loading,
            onTapPokeBall = { pokemonResource ->
                scope.launch {
                    try {
                        val capturing = capturedByPokeBall.none { it.name == pokemonResource.name }
                        localPokeBall.toggle(pokemonResource)
                        // 実際に捕獲できてから表示する
                        if (capturing) {
                            appState.showSnackBar("${pokemonResource.name}を捕獲しました。", null)
                        }
                    } catch (t: Throwable) {
                        handleError(t)
                    }
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navController = navController,
                title = "ポケモン"
            )
        },
        modifier = if (scaffoldPadding != null) Modifier.padding(scaffoldPadding) else Modifier,
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            when {
                uiState.error != null -> ErrorScreen(onReload = { viewModel.reload() })
                uiState.pokemons.isEmpty() -> PokemonListSkeleton()
                else -> PokemonList()
            }
        }
    }
}