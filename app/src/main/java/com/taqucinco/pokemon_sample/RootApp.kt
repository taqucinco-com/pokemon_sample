package com.taqucinco.pokemon_sample

import androidx.compose.material.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.taqucinco.pokemon_sample.feature.pokemon.pokeBall.LocalPokeBallProvider
import com.taqucinco.pokemon_sample.feature.pokemon.pokeBall.PokeBallProvidable
import com.taqucinco.pokemon_sample.ui.hooks.rememberAppState
import com.taqucinco.pokemon_sample.ui.route.RouteNavigation
import com.taqucinco.pokemon_sample.ui.route.TabBottomNavigation

@Composable
fun RootApp(
    pokeBallProvider: PokeBallProvidable
) {
    val appState = rememberAppState()
    val (rootNavHostController, snackbarHostState) = appState
    val pokeBall = remember { pokeBallProvider }

    // ユーザー情報やモンスターボールの捕獲ポケモンなどどの画面でも共通して使う状態は状態管理として共有する
    CompositionLocalProvider(LocalPokeBallProvider provides pokeBall) {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            bottomBar = {
                TabBottomNavigation(
                    navController = rootNavHostController,
                    onNavigateToDestination = {
                        rootNavHostController.navigate(it.route) {
                            popUpTo(rootNavHostController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        ) { paddingValues ->
            RouteNavigation(appState, paddingValues)
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun RootAppPreview() {
//    Pokemon_sampleTheme {
//        RootApp()
//    }
//}