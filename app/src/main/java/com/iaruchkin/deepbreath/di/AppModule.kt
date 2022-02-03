package com.iaruchkin.deepbreath.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
interface AppModule {

//    @Binds
//    fun bindWebNavigator(
//        impl: WebNavigatorImpl,
//    ): WebNavigator
}