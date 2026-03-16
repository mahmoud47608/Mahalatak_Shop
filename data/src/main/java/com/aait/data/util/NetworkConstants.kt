package com.aait.data.util

import okhttp3.MediaType.Companion.toMediaType

object NetworkConstants {
    const val LANGUAGE = "lang"
    const val BEARER = "Bearer "
    const val AUTHORIZATION = "Authorization"
    const val NETWORK_TIMEOUT = 120000L
    val CONTENT_TYPE = "application/json".toMediaType()
}
