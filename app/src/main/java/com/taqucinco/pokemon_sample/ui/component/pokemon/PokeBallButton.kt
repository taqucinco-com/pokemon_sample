package com.taqucinco.pokemon_sample.ui.component.pokemon

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SportsBasketball
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun PokeBallButton(
    onClick: () -> Unit = { },
    capturing: Boolean,
    size: Dp = 32.dp
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.size(size)
    ) {
        Icon(
            imageVector = Icons.Filled.SportsBasketball,
            modifier = Modifier.fillMaxSize(),
            contentDescription = null,
            tint = if (capturing) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.inversePrimary,
        )
    }
}