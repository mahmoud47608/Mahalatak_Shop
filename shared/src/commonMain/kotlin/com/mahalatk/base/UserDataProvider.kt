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
    suspend fun getUser(): AuthData.User? {
        val raw = preferenceRepository.getUserData().firstOrNull()
        if (raw.isNullOrEmpty()) return null
        return try {
            appJson.decodeFromString<AuthData>(raw).user
        } catch (_: Exception) {
            null
        }
    }

    suspend fun getUserName(): String {
        val user = getUser() ?: return ""
        return "${user.firstName.orEmpty()} ${user.lastName.orEmpty()}".trim()
    }

    suspend fun getUserImage(): String {
        return getUser()?.image.orEmpty()
    }
}
