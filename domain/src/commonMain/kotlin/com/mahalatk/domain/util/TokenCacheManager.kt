package com.mahalatk.domain.util

interface TokenCacheManager {
    fun removeToken()
    fun refreshTokenCache()
}
