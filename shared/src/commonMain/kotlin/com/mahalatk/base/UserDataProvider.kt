package com.mahalatk.base

import com.mahalatk.domain.entity.AuthData
import com.mahalatk.domain.repository.PreferenceRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.json.Json

/**
 * Single source of truth for parsing user data from preferences.
 * Eliminates duplicate JSON parsing logic across ViewModels.
 */
class UserDataProvider(
    private val preferenceRepository: PreferenceRepository,
) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun getUser(): AuthData.User? {
        val raw = preferenceRepository.getUserData().firstOrNull()
        if (raw.isNullOrEmpty()) return null
        return try {
            json.decodeFromString<AuthData>(raw).user
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
