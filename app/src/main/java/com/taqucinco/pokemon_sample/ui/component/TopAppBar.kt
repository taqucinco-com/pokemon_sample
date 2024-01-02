package com.taqucinco.pokemon_sample.ui.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    title: String = "",
    navController: NavController,
    onTapBackButton: () -> Unit = { navController.popBackStack() },
    actions: @Composable RowScope.() -> Unit = {},
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.TopAppBar(
        title = { Text(text = title, fontSize = 20.sp) },
        modifier = modifier,
        navigationIcon = {
            if (navController.previousBackStackEntry != null) {
                IconButton(onClick = onTapBackButton) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "戻る"
                    )
                }
            }
        },
        actions = actions
    )
}