package com.aait.domain.usecase.socket

import com.aait.domain.repository.SocketRepository
import javax.inject.Inject

class DisconnectSocketUseCase @Inject constructor(private val socketRepository: SocketRepository) {
    operator fun invoke(): Unit = socketRepository.disconnectSocket()
}