package com.mahalatk.domain.datasource

import kotlinx.coroutines.flow.Flow

interface SecureStorage {
    suspend fun getValue(key: String, default: Any?): Flow<Any?>
    suspend fun setValue(key: String, value: Any?)
}
