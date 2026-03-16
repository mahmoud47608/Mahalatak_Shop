package com.aait.data.datasource

import kotlinx.coroutines.flow.Flow

expect class SecureStorage {
    suspend fun getValue(key: String, default: Any?): Flow<Any?>
    suspend fun setValue(key: String, value: Any?)
}
