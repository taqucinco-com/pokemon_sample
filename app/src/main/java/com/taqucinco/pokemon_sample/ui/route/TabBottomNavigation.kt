package com.taqucinco.pokemon_sample.ui.route

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsBasketball
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

enum class TopLevelDestinations(val route: String) {
    Home(route = "/home"),
    PokeBall(route = "/poke-ball"),
    Settings(route = "/settings"),
}

@Stable
data class TopLevelRoute(
    val destination: TopLevelDestinations,
    val name: String,
    val icon: ImageVector // # e.g. Icons.Filled.Home
)

val topLevelRoutes = listOf(
    TopLevelRoute(TopLevelDestinations.Home, "ホーム", Icons.Filled.Home),
    TopLevelRoute(TopLevelDestinations.PokeBall, "ボール", Icons.Filled.SportsBasketball),
    TopLevelRoute(TopLevelDestinations.Settings, "設定", Icons.Filled.Settings),
)

@Composable
fun TabBottomNavigation(
    navController: NavController,
    onNavigateToDestination: (TopLevelDestinations) -> Unit,
) {
    BottomNavigation {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        topLevelRoutes.forEach { item ->
            val (destination, name, icon) = item
            BottomNavigationItem(
                selected = currentDestination?.route?.contains("^${destination.route}".toRegex()) == true,
                onClick = {
                    onNavigateToDestination(destination)
                },
                icon = { Icon(icon, contentDescription = name) },
                label = { Text(name) },
                alwaysShowLabel = true,
            )
        }
    }
}
