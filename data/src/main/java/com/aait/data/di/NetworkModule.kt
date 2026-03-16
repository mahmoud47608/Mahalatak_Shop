package com.aait.data.di

import com.aait.data.util.NetworkConstants
import com.aait.data.util.NetworkConstants.CONTENT_TYPE
import com.aait.data.util.NetworkConstants.NETWORK_TIMEOUT
import com.aait.data.util.TokenHeaderProvider
import com.aait.domain.repository.PreferenceRepository
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import retrofit2.Converter
import retrofit2.Retrofit
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    @Singleton
    fun converterFactory(): Converter.Factory {
        val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
        return json.asConverterFactory(CONTENT_TYPE)
    }

    @Provides
    @Singleton
    fun loggingInterceptor(): LoggingInterceptor {
        return LoggingInterceptor.Builder()
            .setLevel(Level.BASIC)
            .log(Platform.INFO)
            .request("Request")
            .response("Response")
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthHeaderProvider(
        preferenceRepository: PreferenceRepository
    ): TokenHeaderProvider {
        return TokenHeaderProvider(preferenceRepository)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: LoggingInterceptor,
        tokenHeaderProvider: TokenHeaderProvider,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val original = chain.request()

                val requestBuilder = original.newBuilder()
                    .header(
                        NetworkConstants.LANGUAGE,
                        Locale.getDefault().language
                    )
                    .header(
                        NetworkConstants.AUTHORIZATION,
                        tokenHeaderProvider.getToken()
                    )
                val request = requestBuilder.build()
                return@addInterceptor chain.proceed(request)
            }.connectTimeout(NETWORK_TIMEOUT, TimeUnit.MILLISECONDS)
            .readTimeout(NETWORK_TIMEOUT, TimeUnit.MILLISECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory,
        @StringsModule.RemoteBaseUrl baseUrl: String
    ): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .build()
    }

    @Provides
    @Singleton
    fun provideCoroutineContext(): CoroutineContext = Dispatchers.IO
}
