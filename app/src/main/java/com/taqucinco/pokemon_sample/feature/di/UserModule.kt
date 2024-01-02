package com.taqucinco.pokemon_sample.feature.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import com.taqucinco.pokemon_sample.feature.analytics.FirebaseAnalyticsServable
import com.taqucinco.pokemon_sample.feature.database.SharedPref
import com.taqucinco.pokemon_sample.feature.env.UUIDGeneratable
import com.taqucinco.pokemon_sample.feature.user.UserRepository
import com.taqucinco.pokemon_sample.feature.user.UserRepositoryImpl

@Module(includes = [DatabaseModule::class, EnvModule::class, AnalyticsModule::class])
@InstallIn(ActivityComponent::class)
object UserModule {
    @Provides
    fun provideUserRepository(
        uuidGeneratable: UUIDGeneratable,
        sharedPrefFactory: SharedPref.Factory,
        firebaseAnalyticsService: FirebaseAnalyticsServable
    ): UserRepository {
        return UserRepositoryImpl(
            uuidGeneratable = uuidGeneratable,
            sharedPrefFactory = sharedPrefFactory,
            firebaseAnalyticsService = firebaseAnalyticsService
        )
    }
}