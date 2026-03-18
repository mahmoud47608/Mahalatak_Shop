package com.aait.domain.util

interface TokenCacheManager {
    fun removeToken()
    fun refreshTokenCache()
}
