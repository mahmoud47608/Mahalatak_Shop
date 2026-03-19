package com.mahalatk.data

import com.mahalatk.domain.util.TokenCacheManager
import com.russhwolf.settings.Settings

class IosTokenCacheManager : TokenCacheManager {

    private val settings = Settings()

    override fun refreshTokenCache() {
        // Token is read directly from settings on iOS
    }

    override fun removeToken() {
        settings.remove("token")
    }
}
