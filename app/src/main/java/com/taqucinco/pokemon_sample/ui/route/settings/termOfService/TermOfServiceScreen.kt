package com.taqucinco.pokemon_sample.ui.route.settings.termOfService

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.taqucinco.pokemon_sample.ui.component.TopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermOfServiceScreen(
    navController: NavController,
    scaffoldPadding: PaddingValues? = null,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navController = navController,
                title = "利用規約"
            )
        },
        modifier = if (scaffoldPadding != null) Modifier.padding(scaffoldPadding) else Modifier,
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Text(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}