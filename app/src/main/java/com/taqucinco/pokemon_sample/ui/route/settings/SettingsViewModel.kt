package com.taqucinco.pokemon_sample.ui.route.settings

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    val getValue: Int
        get() {
            return savedStateHandle.get<Int>("key") ?: 0
        }

    fun increment() {
        savedStateHandle["key"] = this.getValue + 1
    }
}