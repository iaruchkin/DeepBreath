package com.iaruchkin.deepbreath.di

import com.iaruchkin.deepbreath.data.datasource.remote.ApiService
import com.iaruchkin.deepbreath.data.repository.AqiRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    /**
     * Provides RemoteDataRepository for access api service method
     */
    @Singleton
    @Provides
    fun provideAqiRepository(
        apiService: ApiService,
    ): AqiRepository {
        return AqiRepository(
            apiService
        )
    }

}