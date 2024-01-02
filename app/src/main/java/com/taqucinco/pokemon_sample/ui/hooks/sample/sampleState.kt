package com.taqucinco.pokemon_sample.ui.hooks.sample

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

// https://developer.android.com/jetpack/compose/state?hl=ja#restore-ui-state
class SomeSaveableState(
    hoge: String = "",
) {
    var hoge by mutableStateOf(hoge)

    companion object {
        // 本来はprimitiveではないStateを含む時にこの手法を用いる
        val Saver: Saver<SomeSaveableState, *> = listSaver(
            save = { saverScope ->
                listOf( saverScope.hoge)
            },
            restore = { list ->
                val data = (list[0] as? String)
                SomeSaveableState(
                    hoge = data ?: ""
                )
            }
        )
    }
}

@Composable
fun rememberSomeSaveableState(
    hoge: String = ""
): SomeSaveableState = rememberSaveable(
    hoge, saver = SomeSaveableState.Saver
) {
    SomeSaveableState(hoge)
}
