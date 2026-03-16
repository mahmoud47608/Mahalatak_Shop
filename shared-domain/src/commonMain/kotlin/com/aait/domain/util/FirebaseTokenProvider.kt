package com.aait.domain.util

interface FirebaseTokenProvider {
    suspend fun getToken(): String
}
