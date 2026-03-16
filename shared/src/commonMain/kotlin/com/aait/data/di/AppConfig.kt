package com.aait.data.di

data class AppConfig(
    val apiKey: String,
    val remoteUrl: String,
    val socketPort: String
) {
    val remoteBaseUrl: String get() = "$remoteUrl/api/"
    val socketBaseUrl: String get() = "$remoteUrl:$socketPort"
}
