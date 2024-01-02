package com.taqucinco.pokemon_sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import com.taqucinco.pokemon_sample.feature.pokemon.pokeBall.PokeBallProvidable
import com.taqucinco.pokemon_sample.feature.user.UserRepository
import com.taqucinco.pokemon_sample.ui.theme.Pokemon_sampleTheme
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var pokeBall: PokeBallProvidable
    @Inject lateinit var userRepo: UserRepository

//    val pokeBallViewModel by viewModels<PokeBallViewModel>(
//        extrasProducer = {
//            defaultViewModelCreationExtras.withCreationCallback<PokeBallViewModel.Factory> {
//                it.create(1)
//            }
//        }
//    )

//    @Inject lateinit var factory: PokeBallViewModel.Factory
//    val pokeBallViewModel: PokeBallViewModel by viewModels<PokeBallViewModel> {
//        PokeBallViewModel.provideFactory(factory, 1)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        runBlocking {
            userRepo.register()
        }

        setContent {
            Pokemon_sampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RootApp(pokeBall)
                }
            }
        }
    }
}
