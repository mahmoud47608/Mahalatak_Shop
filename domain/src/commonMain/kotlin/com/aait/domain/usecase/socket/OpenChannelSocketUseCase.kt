package com.aait.domain.usecase.socket

import com.aait.domain.repository.SocketRepository

class OpenChannelSocketUseCase(private val socketRepository: SocketRepository) {
    operator fun invoke(key: String) = socketRepository.openChannel(key)
}