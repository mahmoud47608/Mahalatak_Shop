package com.aait.data.di

import com.aait.data.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

@InstallIn(SingletonComponent::class)
@Module
object StringsModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class BaseUrl

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class RemoteBaseUrl

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class SocketPort

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class SocketBaseUrl

    @Provides
    @BaseUrl
    fun provideBaseUrl(): String = BuildConfig.REMOTE_URL

    @Provides
    @RemoteBaseUrl
    fun provideRemoteBaseUrl(@BaseUrl baseUrl: String): String = "$baseUrl/api/"

    @Provides
    @SocketPort
    fun provideSocketPort(): String = BuildConfig.SOCKET_PORT

    @Provides
    @SocketBaseUrl
    fun provideSocketBaseUrl(@BaseUrl baseUrl: String, @SocketPort socketPort: String): String =
        "${baseUrl}:${socketPort}"
}
