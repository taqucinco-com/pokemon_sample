package com.taqucinco.pokemon_sample.ui.route.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.taqucinco.pokemon_sample.feature.pokemon.PokemonRemoteRepository
import com.taqucinco.pokemon_sample.feature.pokemon.PokemonResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val remoteRepo: PokemonRemoteRepository,
    private val savedStateHandle: SavedStateHandle,
): ViewModel() {
    private val _uiState = MutableStateFlow(HomeScreenUiState())
    val uiState: StateFlow<HomeScreenUiState> = _uiState.asStateFlow()

    fun fetchPokemon() {
        if (!_uiState.value.remaining) return
        viewModelScope.launch {
            try {
                async(Dispatchers.Main) { _uiState.value = _uiState.value.copy(loading = true) }.await()
                val response = remoteRepo.getList(50, _uiState.value.offset)
                if (_uiState.value.offset > 0 ) delay(1000) // ProgressIndicatorを見せるためにあえて無限スクロール時は遅らせる
                val fetched: List<PokemonResource> = response.results
                val current = _uiState.value.pokemons
                _uiState.value = _uiState.value.copy(
                    pokemons = listOf(*current.toTypedArray(), *fetched.toTypedArray()),
                    remaining = (current.size + fetched.size) < response.count,
                    offset = _uiState.value.offset + 50,
                    loading = false,
                )
            } catch (t: Throwable) {
                _uiState.value = _uiState.value.copy(error = t, loading = false)
                print(t)
            }
        }
    }

    fun reload() {
        _uiState.value = HomeScreenUiState()
        fetchPokemon()
    }

    fun setError(t: Throwable) {
        _uiState.value = _uiState.value.copy(error = t)
    }
}
