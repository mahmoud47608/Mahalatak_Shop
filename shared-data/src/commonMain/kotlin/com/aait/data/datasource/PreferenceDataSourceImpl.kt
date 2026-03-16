package com.aait.data.datasource

import com.aait.data.util.PreferenceConstants
import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class PreferenceDataSourceImpl(
    private val settings: Settings,
    private val secureStorage: SecureStorage
) : PreferenceDataSource {

    override suspend fun getValue(key: String, default: Any?): Flow<Any?> {
        if (key == PreferenceConstants.TOKEN || key == PreferenceConstants.USER_DATA) {
            return secureStorage.getValue(key, default)
        }

        return flowOf(
            when (default) {
                is String -> settings.getStringOrNull(key) ?: default
                is Int -> settings.getIntOrNull(key) ?: default
                is Long -> settings.getLongOrNull(key) ?: default
                is Float -> settings.getFloatOrNull(key) ?: default
                is Double -> settings.getDoubleOrNull(key) ?: default
                is Boolean -> settings.getBooleanOrNull(key) ?: default
                else -> default
            }
        )
    }

    override suspend fun setValue(key: String, value: Any?) {
        if (key == PreferenceConstants.TOKEN || key == PreferenceConstants.USER_DATA) {
            secureStorage.setValue(key, value)
            return
        }

        when (value) {
            is String -> settings.putString(key, value)
            is Int -> settings.putInt(key, value)
            is Long -> settings.putLong(key, value)
            is Float -> settings.putFloat(key, value)
            is Double -> settings.putDouble(key, value)
            is Boolean -> settings.putBoolean(key, value)
            null -> settings.remove(key)
        }
    }
}
