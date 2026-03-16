package com.aait.data.datasource_impl

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.aait.data.datasource.PreferenceDataSource
import com.aait.data.util.PreferenceConstants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class EncryptedPreferenceDataSource @Inject constructor(
    private val context: Context
) : PreferenceDataSource {

    private val encryptedSharedPreferences by lazy {
        createEncryptedSharedPreferences()
    }

    private fun createEncryptedSharedPreferences() =
        EncryptedSharedPreferences.create(
            context,
            ENCRYPTED_PREFS_FILE_NAME,
            createMasterKey(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    private fun createMasterKey(): MasterKey {
        return MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
    }

    override suspend fun getValue(key: String, default: Any?): Flow<Any?> {
        return flowOf(
            try {
                when (default) {
                    is String -> encryptedSharedPreferences.getString(key, default) ?: default
                    is Boolean -> encryptedSharedPreferences.getBoolean(key, default)
                    is Int -> encryptedSharedPreferences.getInt(key, default)
                    is Long -> encryptedSharedPreferences.getLong(key, default)
                    is Float -> encryptedSharedPreferences.getFloat(key, default)
                    is Double -> {
                        // SharedPreferences doesn't support Double natively, store as String
                        encryptedSharedPreferences.getString(key, default.toString())
                            ?.toDoubleOrNull() ?: default
                    }
                    else -> default
                }
            } catch (e: Exception) {
                e.printStackTrace()
                default
            }
        )
    }

    override suspend fun setValue(key: String, value: Any?) {
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
            // Log error but don't crash
            e.printStackTrace()
        }
    }

    fun removeKey(key: String) {
        try {
            encryptedSharedPreferences.edit { remove(key) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun clearToken() {
        removeKey(PreferenceConstants.TOKEN)
    }

    fun clearAllData() {
        try {
            encryptedSharedPreferences.edit { clear() }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun contains(key: String): Boolean {
        return try {
            encryptedSharedPreferences.contains(key)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    companion object {
        private const val ENCRYPTED_PREFS_FILE_NAME = "com.aait.base.encrypted_prefs"
    }
}
