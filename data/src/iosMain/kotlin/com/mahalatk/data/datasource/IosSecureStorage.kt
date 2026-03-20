package com.mahalatk.data.datasource

import com.mahalatk.domain.datasource.SecureStorage
import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class IosSecureStorage : SecureStorage {

    private val settings = Settings()

    override suspend fun getValue(key: String, default: Any?): Flow<Any?> {
        return flowOf(
            when (default) {
                is String -> settings.getStringOrNull(key) ?: default
                is Boolean -> settings.getBooleanOrNull(key) ?: default
                is Int -> settings.getIntOrNull(key) ?: default
                is Long -> settings.getLongOrNull(key) ?: default
                is Float -> settings.getFloatOrNull(key) ?: default
                is Double -> settings.getDoubleOrNull(key) ?: default
                else -> default
            }
        )
    }

    override suspend fun setValue(key: String, value: Any?) {
        when (value) {
            is String -> settings.putString(key, value)
            is Boolean -> settings.putBoolean(key, value)
            is Int -> settings.putInt(key, value)
            is Long -> settings.putLong(key, value)
            is Float -> settings.putFloat(key, value)
            is Double -> settings.putDouble(key, value)
            null -> settings.remove(key)
        }
    }
}
