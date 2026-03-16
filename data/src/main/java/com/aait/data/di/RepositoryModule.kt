package com.aait.data.di

import com.aait.data.remote.ChatEndPoint
import com.aait.data.remote.HomeEndPoint
import com.aait.data.repo_impl.ChatRepositoryImpl
import com.aait.data.repo_impl.HomeRepositoryImpl
import com.aait.domain.repository.ChatRepository
import com.aait.domain.repository.HomeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideHomeRepo(homeEndPoint: HomeEndPoint): HomeRepository =
        HomeRepositoryImpl(homeEndPoint)

    @Provides
    @Singleton
    fun provideChatRepo(
        moreEndPoint: ChatEndPoint
    ): ChatRepository = ChatRepositoryImpl(moreEndPoint)
}