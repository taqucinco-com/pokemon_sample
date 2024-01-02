package com.taqucinco.pokemon_sample.ui.route.pokeBall

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.taqucinco.pokemon_sample.feature.pokemon.Pokemon
import com.taqucinco.pokemon_sample.feature.pokemon.pokeBall.LocalPokeBallProvider
import com.taqucinco.pokemon_sample.ui.component.TopAppBar
import com.taqucinco.pokemon_sample.ui.hooks.pokemon.rememberPokemonsState
import com.taqucinco.pokemon_sample.ui.component.screen.ErrorScreen
import com.taqucinco.pokemon_sample.ui.hooks.AppState
import com.taqucinco.pokemon_sample.ui.hooks.sample.rememberSomeSaveableState
import com.taqucinco.pokemon_sample.ui.route.pokeBall._component.PokeBallGrid
import com.taqucinco.pokemon_sample.ui.route.pokeBall._component.PokeBallGridSkeleton
import com.taqucinco.pokemon_sample.ui.route.pokeBall._component.PokeBallNoPokemon
import kotlinx.coroutines.launch

/// モンスターボール画面はHooksの概念を採用し、ViewModelはDIで得たInterfaceをHooksに提供する役割に限定する
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokeBallScreen(
    navController: NavController,
    scaffoldPadding: PaddingValues? = null,
    appState: AppState,
    viewModel: PokeBallViewProvider = hiltViewModel<PokeBallViewModel>(),
) {
    val scope = rememberCoroutineScope()
    val localPokeBall = LocalPokeBallProvider.current
    val capturedResource = localPokeBall.capturedByPokeBall()
    val (list, reload, getLoaded, loading, hasError) = rememberPokemonsState(localPokeBall.state, remoteRepo = viewModel.provideRemoteRepo)

    val saveableState = rememberSomeSaveableState() // このstateはタブ移動などで画面を一度破棄しても復元される

    val remove: (Pokemon) -> Unit = { pokemon ->
        val matched = capturedResource.find { resource -> resource.name == pokemon.name }
        matched?.let {
            scope.launch {
                localPokeBall.toggle(it)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navController = navController,
                title = "モンスターボール"
            )
        },
        modifier = if (scaffoldPadding != null) Modifier.padding(scaffoldPadding) else Modifier,
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when {
                capturedResource.isEmpty() -> PokeBallNoPokemon()
                hasError() -> ErrorScreen { reload() }
                loading() -> PokeBallGridSkeleton(num = list.size)
                else -> PokeBallGrid(
                    pokemons = getLoaded(),
                    onClick = {
                        remove(it)
                        scope.launch {
                            appState.showSnackBar("リリースしました。", null)
                        }
                    }
                )
            }
        }
    }
}
