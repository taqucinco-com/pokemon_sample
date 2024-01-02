package com.taqucinco.pokemon_sample.feature.di

import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import com.taqucinco.pokemon_sample.feature.analytics.FirebaseAnalyticsServable
import com.taqucinco.pokemon_sample.feature.analytics.FirebaseAnalyticsService

@Module
@InstallIn(ActivityComponent::class)
object AnalyticsModule {
    @Provides
    fun provideFirebaseAnalyticsService(
    ): FirebaseAnalyticsServable {
        return FirebaseAnalyticsService(fa = Firebase.analytics)
    }
}
