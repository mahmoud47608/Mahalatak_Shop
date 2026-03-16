package com.aait.data.di

import com.aait.data.remote.HomeEndPoint
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomeNetworkModule {

    @Singleton
    @Provides
    fun providesHomeEndPoint(retrofit: Retrofit): HomeEndPoint {
        return retrofit.create(HomeEndPoint::class.java)
    }
}