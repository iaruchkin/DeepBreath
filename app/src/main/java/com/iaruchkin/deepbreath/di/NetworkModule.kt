package com.iaruchkin.deepbreath.di

import com.iaruchkin.deepbreath.data.aqi.remote.AqiEndpoint
import com.iaruchkin.deepbreath.data.core.AqiHttpClient
import com.iaruchkin.deepbreath.data.core.AqiHttpClientImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NetworkModule {

    @Binds
    @Singleton
    fun bindMovieDbClient(
        impl: AqiHttpClientImpl,
    ): AqiHttpClient
}

@Module
@InstallIn(SingletonComponent::class)
object ApiWrapperModule {

    @Provides
    @Singleton
    fun provideMoviesApi(client: AqiHttpClient): AqiEndpoint = client.aqiApi
}
