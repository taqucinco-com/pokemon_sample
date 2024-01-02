package com.taqucinco.pokemon_sample.feature.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.taqucinco.pokemon_sample.feature.env.UUIDGeneratable
import java.util.UUID

@Module
@InstallIn(SingletonComponent::class)
object EnvModule {
    @Provides
    fun provideUUIDGenerater(
    ): UUIDGeneratable {
        return object : UUIDGeneratable {
            override fun generate(): UUID = UUID.randomUUID()
        }
    }
}
