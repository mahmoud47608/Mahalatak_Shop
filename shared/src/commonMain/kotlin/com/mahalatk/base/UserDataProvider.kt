package com.mahalatk.base

import com.mahalatk.domain.entity.AuthData
import com.mahalatk.domain.repository.PreferenceRepository
import com.mahalatk.domain.util.appJson
import kotlinx.coroutines.flow.firstOrNull

/**
 * Single source of truth for parsing user data from preferences.
 * Uses the shared [appJson] instance — no duplicate Json allocations.
 */
class UserDataProvider(
    private val preferenceRepository: PreferenceRepository,
) {
    suspend fun getAuthData(): AuthData? {
        val raw = preferenceRepository.getUserData().firstOrNull()
        if (raw.isNullOrEmpty()) return null
        return try {
            appJson.decodeFromString<AuthData>(raw)
        } catch (_: Exception) {
            null
        }
    }

    suspend fun getUserName(): String {
        val data = getAuthData() ?: return ""
        // API returns "name" field directly, or fallback to firstName + lastName
        return data.name
            ?: "${data.firstName.orEmpty()} ${data.lastName.orEmpty()}".trim()
    }

    suspend fun getUserImage(): String {
        return getAuthData()?.image.orEmpty()
    }
}
