package com.taqucinco.pokemon_sample.feature.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import com.taqucinco.pokemon_sample.feature.analytics.FirebaseAnalyticsServable
import com.taqucinco.pokemon_sample.feature.database.DatabaseFactory
import com.taqucinco.pokemon_sample.feature.http.rest.RestClientInterface
import com.taqucinco.pokemon_sample.feature.pokemon.PokeBallRepository
import com.taqucinco.pokemon_sample.feature.pokemon.PokeBallRepositoryImpl
import com.taqucinco.pokemon_sample.feature.pokemon.PokemonRemoteRepository
import com.taqucinco.pokemon_sample.feature.pokemon.PokemonRemoteRepositoryImpl
import com.taqucinco.pokemon_sample.feature.pokemon.PokeBallServable
import com.taqucinco.pokemon_sample.feature.pokemon.PokeBallService
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
    fun providePokeBallProvider(
        service: PokeBallServable,
        fa: FirebaseAnalyticsServable
    ): PokeBallProvidable {
        return PokeBallProvider(service, fa)
    }

    @Provides
    fun providePokeBallRepository(
        dbFactory: DatabaseFactory
    ): PokeBallRepository {
        return PokeBallRepositoryImpl(dbFactory)
    }

    @Provides
    fun providePokemonService(
        repo: PokeBallRepository
    ): PokeBallServable {
        return PokeBallService(repo = repo)
    }
}