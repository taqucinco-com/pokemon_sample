package com.taqucinco.pokemon_sample.ui.hooks.counter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

// カスタムhooksとほぼ同じ
// https://zenn.dev/izuchy/articles/5cec4c2318caff6490de

@Stable
data class CounterState(
    val count: Int,
    val increment: () -> Unit,
)

@Composable
fun rememberCounterState(): CounterState {
    var count by remember { mutableIntStateOf(0) }

    return remember(count) {
        CounterState(
            count = count,
            increment = { count + 1 }
        )
    }
}
