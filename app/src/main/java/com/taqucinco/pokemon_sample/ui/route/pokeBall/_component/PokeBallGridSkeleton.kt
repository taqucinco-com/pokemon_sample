package com.taqucinco.pokemon_sample.ui.route.pokeBall._component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer
import com.taqucinco.pokemon_sample.ui.component.pokemon.PokeBallButton

@Composable
fun PokeBallGridSkeleton(
    num: Int
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2)
    ) {
        items(num) {
            PokeBallGridSkeletonItem()
        }
    }
}

@Composable
private fun PokeBallGridSkeletonItem() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Filled.Image,
            contentDescription = "ローディング",
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(shape = MaterialTheme.shapes.medium)
                .shimmer(),
            tint = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "**********",
            modifier = Modifier.shimmer()
        )
        Spacer(modifier = Modifier.height(8.dp))
        PokeBallButton(
            capturing = false,
            size = 48.dp
        )
    }
}