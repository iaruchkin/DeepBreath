package com.iaruchkin.deepbreath.di

import com.iaruchkin.deepbreath.data.aqi.local.AqiLocalDataSource
import com.iaruchkin.deepbreath.data.aqi.local.AqiLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface AqiModule {

    @Binds
    fun bindAqiRemoteDataSource(
        impl: AqiRemoteDataSourceImpl,
    ): AqiRemoteDataSource

    @Binds
    fun bindAqiLocalDataSource(
        impl: AqiLocalDataSourceImpl,
    ): AqiLocalDataSource

    @Binds
    fun bindAqiRepository(
        impl: AqiRepositoryImpl,
    ): AqiRepository
}
