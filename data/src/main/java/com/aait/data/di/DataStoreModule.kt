package com.aait.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.preferencesDataStoreFile
import com.aait.data.datasource.PreferenceDataSource
import com.aait.data.datasource_impl.EncryptedPreferenceDataSource
import com.aait.data.datasource_impl.PreferenceDataSourceImpl
import com.aait.data.repo_impl.PreferenceRepositoryImpl
import com.aait.domain.repository.PreferenceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton



@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    private const val PREFERENCE_NAME = "base_preferences"

    val Context.dataStore by preferencesDataStore(
        name = PREFERENCE_NAME
    )
    @Provides
    @Singleton
    fun providePreferencesDataStore(
        @ApplicationContext appContext: Context
    ): DataStore<Preferences> {
        return appContext.dataStore
    }

    @Provides
    @Singleton
    fun provideEncryptedPreferenceDataSource(
        @ApplicationContext appContext: Context
    ): EncryptedPreferenceDataSource {
        return EncryptedPreferenceDataSource(appContext)
    }

    @Provides
    @Singleton
    fun providePreferencesDataSource(
        dataStore: DataStore<Preferences>,
        encryptedPreferenceDataSource: EncryptedPreferenceDataSource
    ): PreferenceDataSource {
        return PreferenceDataSourceImpl(
            dataStore = dataStore,
            encryptedPreferenceDataSource = encryptedPreferenceDataSource
        )
    }

    @Provides
    @Singleton
    fun providePreferencesRepository(
        preferencesDataSource: PreferenceDataSource
    ): PreferenceRepository {
        return PreferenceRepositoryImpl(preferencesDataSource)
    }
}