package com.taqucinco.pokemon_sample.ui.route

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.taqucinco.pokemon_sample.ui.hooks.AppState
import com.taqucinco.pokemon_sample.ui.route.home.HomeScreen
import com.taqucinco.pokemon_sample.ui.route.pokeBall.PokeBallScreen
import com.taqucinco.pokemon_sample.ui.route.settings.SettingsScreen
import com.taqucinco.pokemon_sample.ui.route.settings.termOfService.TermOfServiceScreen

@Composable
fun RouteNavigation(
    appState: AppState,
    scaffoldPadding: PaddingValues? = null,
) {
    val (root, _) = appState
    NavHost(navController = root, startDestination = TopLevelDestinations.Home.route) {
        composable(TopLevelDestinations.Home.route) {
            val navHostController = rememberNavController()
            HomeScreen(
                navController = navHostController,
                scaffoldPadding = scaffoldPadding,
                appState = appState,
            )
        }

        composable(TopLevelDestinations.PokeBall.route) {
            val navHostController = rememberNavController()
            PokeBallScreen(
                navController = navHostController,
                scaffoldPadding = scaffoldPadding,
                appState = appState,
            )
        }

        composable(TopLevelDestinations.Settings.route) {
            // タブ内のNavHostControllerはcomposableの中で定義しないと以下のエラーがスローされる
            // ou cannot access the NavBackStackEntry's ViewModels after the NavBackStackEntry is destroyed
            val navHostController = rememberNavController()
            NavHost(navController = navHostController, startDestination = "/#") {
                composable("/#") {
                    SettingsScreen(
                        navController = navHostController,
                        scaffoldPadding = scaffoldPadding,
                        appState = appState,
                        onNavigate = { navHostController.navigate("/term-of-service")}
                    )
                }

                composable("/term-of-service") {
                    TermOfServiceScreen(
                        navController = navHostController,
                        scaffoldPadding = scaffoldPadding,
                    )
                }
            }
        }
    }
}