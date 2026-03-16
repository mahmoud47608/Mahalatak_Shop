package com.aait.data.repo_impl

import com.aait.domain.repository.PreferenceRepository
import com.aait.domain.repository.SocketRepository

expect fun createSocketRepository(
    socketBaseUrl: String,
    preferenceRepository: PreferenceRepository
): SocketRepository
