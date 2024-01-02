package com.taqucinco.pokemon_sample.feature.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import com.taqucinco.pokemon_sample.feature.analytics.FirebaseAnalyticsServable
import com.taqucinco.pokemon_sample.feature.database.DatabaseFactory
import com.taqucinco.pokemon_sample.feature.http.rest.RestClientInterface
import com.taqucinco.pokemon_sample.feature.pokemon.PokemonLocalRepository
import com.taqucinco.pokemon_sample.feature.pokemon.PokemonLocalRepositoryImpl
import com.taqucinco.pokemon_sample.feature.pokemon.PokemonRemoteRepository
import com.taqucinco.pokemon_sample.feature.pokemon.PokemonRemoteRepositoryImpl
import com.taqucinco.pokemon_sample.feature.pokemon.pokeBall.PokeBallProvidable
import com.taqucinco.pokemon_sample.feature.pokemon.pokeBall.PokeBallProvider

@Module(includes = [HttpModule::class])
@InstallIn(ViewModelComponent::class)
object PokemonRemoteModule {
    @Provides
    fun providePokemonRemoteRepository(
        @NormalRestClient client: RestClientInterface
    ): PokemonRemoteRepository {
        return PokemonRemoteRepositoryImpl(restClient = client)
    }
}

@Module(includes = [DatabaseModule::class])
@InstallIn(ActivityComponent::class)
object PokemonLocalModule {
    @Provides
    fun providePokemonLocalRepository(
        dbFactory: DatabaseFactory
    ): PokemonLocalRepository {
        return PokemonLocalRepositoryImpl(dbFactory = dbFactory)
    }

    @Provides
    fun providePokeBall(
        repo: PokemonLocalRepository,
        fa: FirebaseAnalyticsServable
    ): PokeBallProvidable {
        return PokeBallProvider(repo, fa)
    }
}