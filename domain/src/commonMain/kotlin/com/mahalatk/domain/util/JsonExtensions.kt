package com.mahalatk.domain.util

import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

/**
 * Shared Json instance and extension functions.
 * Available to domain, data, and presentation layers.
 */
val appJson = Json { ignoreUnknownKeys = true }

inline fun <reified T> T.toJson(): String =
    appJson.encodeToString(this)

inline fun <reified T : Any> String.fromJson(): T =
    appJson.decodeFromString(this)

inline fun <reified T : Any> String.fromJsonArray(): List<T> =
    appJson.decodeFromString(serializer<List<T>>(), this)
