package com.taqucinco.pokemon_sample.feature.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.taqucinco.pokemon_sample.feature.database.DatabaseFactory
import com.taqucinco.pokemon_sample.feature.database.DatabaseFactoryImpl
import com.taqucinco.pokemon_sample.feature.database.SharedPref

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    fun provideDatabaseFactory(
        @ApplicationContext context: Context
    ): DatabaseFactory {
        return DatabaseFactoryImpl(context = context)
    }

    @Provides
    fun provideSharedPrefFactory(
        @ApplicationContext context: Context
    ): SharedPref.Factory {
        return object : SharedPref.Factory {
            override fun build(): SharedPreferences {
                return context.getSharedPreferences(SharedPref.prefName, Context.MODE_PRIVATE)
            }
        }
    }
}