package com.taqucinco.pokemon_sample.ui.hooks

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun createHavHostController(): NavHostController {
    return rememberNavController()
}

@Composable
fun rememberAppState(): AppState {
    val rootNavHostController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    return remember(
        rootNavHostController,
        snackbarHostState,
    ) {
        AppState(
            rootNavHostController = rootNavHostController,
            snackbarHostState = snackbarHostState,
        )
    }
}

@Stable
data class AppState(
    val rootNavHostController: NavHostController,
    val snackbarHostState: SnackbarHostState,
) {
    val showSnackBar: suspend (String, String?) -> SnackbarResult = { message, action ->
        snackbarHostState.showSnackbar(
            message = message,
            actionLabel = action,
            duration = SnackbarDuration.Short,
        )
    }
}