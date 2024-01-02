package com.taqucinco.pokemon_sample.feature.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import com.taqucinco.pokemon_sample.feature.http.rest.RestClient
import com.taqucinco.pokemon_sample.feature.http.rest.RestClientInterface
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NormalRestClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LongTimeoutRestClient

@Module
@InstallIn(ViewModelComponent::class)
object HttpModule {
    @NormalRestClient
    @Provides
    fun provideNormalRestClient(
    ): RestClientInterface {
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(10, TimeUnit.SECONDS)
        builder.readTimeout(10, TimeUnit.SECONDS)
        builder.writeTimeout(10, TimeUnit.SECONDS)
        builder.addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        val built = builder.build()
        return RestClient(okHttpClient = built)
    }

    @LongTimeoutRestClient
    @Provides
    fun provideLongTimeRestClient(
    ): RestClientInterface {
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(30, TimeUnit.SECONDS)
        builder.readTimeout(30, TimeUnit.SECONDS)
        builder.writeTimeout(30, TimeUnit.SECONDS)
        builder.addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        val built = builder.build()
        return RestClient(okHttpClient = built)
    }
}