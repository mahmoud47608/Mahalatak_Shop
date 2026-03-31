package com.mahalatk.data.util

import com.mahalatk.domain.repository.PreferenceRepository
import com.mahalatk.domain.util.TokenCacheManager
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.concurrent.Volatile

class TokenHeaderProvider(
    private val preferenceRepository: PreferenceRepository
) : TokenCacheManager {

    @Volatile
    private var cachedToken: String = ""

    /** Signals when the first token load completes so [getToken] can await it. */
    private var initialLoad = CompletableDeferred<Unit>()

    // App-lifetime scope (singleton via Koin). Cancelled in destroy() if needed.
    private val supervisorJob = SupervisorJob()
    private val scope = CoroutineScope(supervisorJob + Dispatchers.Default)

    @Volatile
    private var cacheJob: Job? = null

    fun destroy() {
        supervisorJob.cancel()
    }

    init {
        refreshTokenCache()
    }

    /**
     * Returns the cached bearer token, awaiting the initial load if it hasn't
     * completed yet. This eliminates the race where [getToken] is called before
     * the coroutine in [refreshTokenCache] has finished reading from preferences.
     */
    suspend fun getToken(): String {
        initialLoad.await()
        return if (cachedToken.isNotEmpty()) {
            "${NetworkConstants.BEARER}$cachedToken"
        } else {
            ""
        }
    }

    override fun refreshTokenCache() {
        if (cacheJob?.isActive == true) return
        if (initialLoad.isCompleted) {
            initialLoad = CompletableDeferred()
        }
        cacheJob = scope.launch {
            cachedToken = preferenceRepository.getToken().first()
            initialLoad.complete(Unit)
        }
    }

    override fun removeToken() {
        cachedToken = ""
        // Reset the deferred so the next refresh is properly awaited
        if (initialLoad.isCompleted) {
            initialLoad = CompletableDeferred()
        }
        initialLoad.complete(Unit)
    }
}
