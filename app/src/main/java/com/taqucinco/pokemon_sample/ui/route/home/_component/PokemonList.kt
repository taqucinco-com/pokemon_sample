package com.taqucinco.pokemon_sample.ui.route.home._component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.taqucinco.pokemon_sample.feature.pokemon.PokemonResource
import com.taqucinco.pokemon_sample.ui.component.pokemon.PokeBallButton
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

interface ListedPokemon: PokemonResource {
    val captured: Boolean
}

@Composable
fun PokemonList(
    pokemonList: List<ListedPokemon>,
    scrollThresholdFromBottom: Int = 15, // 表示している領域の残り個数がこの数になったらonScrollを発火
    onScrollThreshold: () -> Unit = { },
    onTapPokeBall:(PokemonResource) -> Unit = { },
    loading: Boolean = false,
) {
    val listState = rememberLazyListState()

    // Stateを別のStateに変換して監視対象を絞りパフォーマンスをあげる
    val layoutInfo by remember { derivedStateOf {
        val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.last().index
        val total = listState.layoutInfo.totalItemsCount
        return@derivedStateOf Pair(lastVisibleItemIndex, total)
    } }

    LaunchedEffect(listState) {
        snapshotFlow { layoutInfo } // stateをFlowに変換
            .distinctUntilChanged()
            .map {
                val (lastVisibleItemIndex, total) = it
                if (lastVisibleItemIndex != -1 && total > scrollThresholdFromBottom) {
                    val lastVisibleItemFromEnd = total - lastVisibleItemIndex - 1
                    lastVisibleItemFromEnd <= scrollThresholdFromBottom
                } else {
                    false
                }
            }
            .distinctUntilChanged()
            .filter { it }
            .collect {
                print("last less than $scrollThresholdFromBottom")
                onScrollThreshold()
            }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxWidth()
    ) {
        items(pokemonList) {
            PokemonListItem(it, onClick = { onTapPokeBall(it) })
            Divider(color = Color.Black)
        }
        
        if (loading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
fun PokemonListItem(
    resource: ListedPokemon,
    onClick: () -> Unit = { }
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("${resource.name}",
            modifier = Modifier
                .padding(8.dp)
                .height(32.dp)
        )
        PokeBallButton(
            onClick = onClick,
            capturing = resource.captured
        )
    }
}
