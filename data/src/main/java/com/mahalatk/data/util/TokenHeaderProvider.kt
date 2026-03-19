package com.mahalatk.data.util

import com.mahalatk.domain.repository.PreferenceRepository
import com.mahalatk.domain.util.TokenCacheManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class TokenHeaderProvider(
    private val preferenceRepository: PreferenceRepository
) : TokenCacheManager {

    @Volatile
    private var cachedToken: String = ""

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

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

    override fun refreshTokenCache() {
        if (cacheJob?.isActive == true) return
        cacheJob = scope.launch {
            cachedToken = preferenceRepository.getToken().first()
        }
    }

    override fun removeToken() {
        cachedToken = ""
    }
}
