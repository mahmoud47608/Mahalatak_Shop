package com.aait.domain.util

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

val json = Json { ignoreUnknownKeys = true }

inline fun <reified T> T.toJson(): String =
    json.encodeToString(this)

inline fun <reified T : Any> String.fromJson(): T =
    json.decodeFromString(this)

inline fun <reified T : Any> String.fromJsonArray(): List<T> =
    json.decodeFromString(serializer<List<T>>(), this)
