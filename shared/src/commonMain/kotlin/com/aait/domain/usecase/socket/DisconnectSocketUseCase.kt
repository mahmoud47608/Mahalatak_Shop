package com.aait.domain.usecase.socket

import com.aait.domain.repository.SocketRepository


class DisconnectSocketUseCase(private val socketRepository: SocketRepository) {
    operator fun invoke(): Unit = socketRepository.disconnectSocket()
}