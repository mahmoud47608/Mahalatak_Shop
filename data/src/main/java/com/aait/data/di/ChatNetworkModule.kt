package com.aait.data.di

import com.aait.data.remote.ChatEndPoint
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ChatNetworkModule {

    @Singleton
    @Provides
    fun providesChatEndPoint(retrofit: Retrofit): ChatEndPoint {
        return retrofit.create(ChatEndPoint::class.java)
    }
}