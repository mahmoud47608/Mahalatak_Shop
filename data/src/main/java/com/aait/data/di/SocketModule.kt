package com.aait.data.di

import com.aait.data.di.StringsModule.SocketBaseUrl
import com.aait.data.repo_impl.SocketRepositoryImpl
import com.aait.domain.repository.PreferenceRepository
import com.aait.domain.repository.SocketRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SocketModule {

    @Provides
    @Singleton
    fun provideSocketRepository(
        @SocketBaseUrl socketBaseUrl: String,
        preferenceRepository: PreferenceRepository
    ): SocketRepository = SocketRepositoryImpl(socketBaseUrl, preferenceRepository)
}