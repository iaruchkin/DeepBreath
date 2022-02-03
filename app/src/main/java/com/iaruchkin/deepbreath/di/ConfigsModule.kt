package com.iaruchkin.deepbreath.di

import com.iaruchkin.deepbreath.BuildConfig
import com.iaruchkin.deepbreath.core.AqiUrlProvider
import com.iaruchkin.deepbreath.core.BuildConfigProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ConfigsModule {

    @Provides
    fun provideBuildConfigProvider(): BuildConfigProvider =
        BuildConfigProvider(
            debug = BuildConfig.DEBUG,
            appId = BuildConfig.APPLICATION_ID,
            buildType = BuildConfig.BUILD_TYPE,
            flavor = "",
            versionCode = BuildConfig.VERSION_CODE,
            versionName = BuildConfig.VERSION_NAME
        )

    @Provides
    fun provideAqiUrlProvider(): AqiUrlProvider =
        AqiUrlProvider(
            baseUrl = BuildConfig.AQI_URL,
            apiKey = BuildConfig.AQI_API_KEY
        )
}