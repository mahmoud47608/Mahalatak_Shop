package com.aait.domain.usecase.socket

import com.aait.domain.repository.SocketRepository
import javax.inject.Inject

class OpenChannelSocketUseCase @Inject constructor(private val socketRepository: SocketRepository) {
    operator fun invoke(key: String) = socketRepository.openChannel(key)
}