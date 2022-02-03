package com.iaruchkin.deepbreath.di

import android.content.Context
import com.iaruchkin.deepbreath.data.core.db.DeepBreathDb
import com.iaruchkin.deepbreath.room.daos.AqiDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideMoviesDb(
        @ApplicationContext appContext: Context
    ): DeepBreathDb = DeepBreathDb.create(appContext)

    @Provides
    fun provideAqiDao(deepBreathDb: DeepBreathDb): AqiDao = deepBreathDb.aqiDao()

}