package com.taqucinco.pokemon_sample.ui.route.pokeBall._component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.taqucinco.pokemon_sample.feature.pokemon.Pokemon
import com.taqucinco.pokemon_sample.ui.component.pokemon.PokeBallButton

@Composable
fun PokeBallGrid(
    pokemons: List<Pokemon>,
    onClick: (Pokemon) -> Unit = { }
) {
    val gridState = rememberLazyGridState()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = gridState
    ) {
        items(pokemons) {
            PokeBallGridItem(it, onClick = { onClick(it) })
        }
    }
}

@Composable
private fun PokeBallGridItem(
    pokemon: Pokemon,
    onClick: () -> Unit = { }
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = pokemon.sprites.frontDefault,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(shape = MaterialTheme.shapes.medium),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "${pokemon.name}")
        Spacer(modifier = Modifier.height(8.dp))
        PokeBallButton(
            onClick = onClick,
            capturing = true,
            size = 48.dp
        )
    }
}