package com.mahalatk.data.platform

expect object AppConfig {
    val baseUrl: String
    val apiKey: String
    val socketPort: String
}
