package com.aait.data.util

import com.aait.domain.repository.PreferenceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenHeaderProvider @Inject constructor(
    private val preferenceRepository: PreferenceRepository
) {

    @Volatile
    private var cachedToken: String = ""

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @Volatile
    private var cacheJob: Job? = null

    init {
        refreshTokenCache()
    }

    fun getToken(): String {
        return if (cachedToken.isNotEmpty()) {
            "${NetworkConstants.BEARER}$cachedToken"
        } else {
            ""
        }
    }

    fun refreshTokenCache() {
        if (cacheJob?.isActive == true) return
        cacheJob = scope.launch {
            cachedToken = preferenceRepository.getToken().first()
        }
    }

    fun removeToken() {
        cachedToken = ""
    }
}
