package com.mahalatk.data.datasource

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class SecureStorage(private val context: Context) {

    private val encryptedSharedPreferences by lazy {
        EncryptedSharedPreferences.create(
            context,
            ENCRYPTED_PREFS_FILE_NAME,
            MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    suspend fun getValue(key: String, default: Any?): Flow<Any?> {
        return flowOf(
            try {
                when (default) {
                    is String -> encryptedSharedPreferences.getString(key, default) ?: default
                    is Boolean -> encryptedSharedPreferences.getBoolean(key, default)
                    is Int -> encryptedSharedPreferences.getInt(key, default)
                    is Long -> encryptedSharedPreferences.getLong(key, default)
                    is Float -> encryptedSharedPreferences.getFloat(key, default)
                    is Double -> encryptedSharedPreferences.getString(key, default.toString())
                        ?.toDoubleOrNull() ?: default

                    else -> default
                }
            } catch (e: Exception) {
                e.printStackTrace()
                default
            }
        )
    }

    suspend fun setValue(key: String, value: Any?) {
        try {
            encryptedSharedPreferences.edit {
                when (value) {
                    is String -> putString(key, value)
                    is Boolean -> putBoolean(key, value)
                    is Int -> putInt(key, value)
                    is Long -> putLong(key, value)
                    is Float -> putFloat(key, value)
                    is Double -> putString(key, value.toString())
                    null -> remove(key)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val ENCRYPTED_PREFS_FILE_NAME = "com.mahalatk.encrypted_prefs"
    }
}
