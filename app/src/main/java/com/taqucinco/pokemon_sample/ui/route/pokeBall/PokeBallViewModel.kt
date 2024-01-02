package com.taqucinco.pokemon_sample.ui.route.pokeBall

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import com.taqucinco.pokemon_sample.feature.pokemon.PokemonRemoteRepository
import javax.inject.Inject

interface PokeBallViewProvider {
    val provideRemoteRepo: PokemonRemoteRepository
}

@HiltViewModel
class PokeBallViewModel @Inject constructor(
    private val remoteRepo: PokemonRemoteRepository,
    private val savedStateHandle: SavedStateHandle,
): ViewModel(), PokeBallViewProvider {
    override val provideRemoteRepo = remoteRepo
}

//@HiltViewModel(assistedFactory = PokeBallViewModel.Factory::class)
//class PokeBallViewModel @AssistedInject constructor(
//    savedStateHandle: SavedStateHandle,
//    @Assisted val id: Int = 0,
//): ViewModel() {
//
//    @AssistedFactory
//    interface Factory {
//        fun create(id: Int): PokeBallViewModel
//    }
//
//    companion object {
//        fun provideFactory(
//            assistedFactory: Factory,
//            id: Int,
//        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
//            override fun <T : ViewModel> create(modelClass: Class<T>): T {
//                @Suppress("UNCHECKED_CAST")
//                return assistedFactory.create(id) as T
//            }
//        }
//    }
//}