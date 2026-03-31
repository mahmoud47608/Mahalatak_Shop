package com.mahalatk.data.util

import com.mahalatk.domain.repository.PreferenceRepository
import com.mahalatk.domain.util.TokenCacheManager
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.concurrent.Volatile

class TokenHeaderProvider(
    private val preferenceRepository: PreferenceRepository
) : TokenCacheManager {

    @Volatile
    private var cachedToken: String = ""

    /** Signals when the first token load completes so [getToken] can await it. */
    private var initialLoad = CompletableDeferred<Unit>()

    /** Mutex to protect atomic read-modify-write of [initialLoad] and [cachedToken]. */
    private val mutex = Mutex()

    // App-lifetime scope (singleton via Koin). Cancelled in destroy().
    private val supervisorJob = SupervisorJob()
    private val scope = CoroutineScope(supervisorJob + Dispatchers.Default)

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
        scope.launch {
            mutex.withLock {
                cachedToken = preferenceRepository.getToken().first()
                if (!initialLoad.isCompleted) {
                    initialLoad.complete(Unit)
                }
            }
        }
    }

    override fun removeToken() {
        scope.launch {
            mutex.withLock {
                cachedToken = ""
                initialLoad = CompletableDeferred()
            }
        }
    }
}
