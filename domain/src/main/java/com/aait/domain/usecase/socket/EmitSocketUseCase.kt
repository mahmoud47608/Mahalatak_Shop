package com.aait.domain.usecase.socket

import com.aait.domain.repository.SocketRepository
import javax.inject.Inject

class EmitSocketUseCase @Inject constructor(private val socketRepository: SocketRepository) {
    operator fun invoke(key: String, data: MutableMap<Any, Any>) = socketRepository.emit(key, data)
}