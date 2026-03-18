package com.aait.domain.usecase.socket

import com.aait.domain.repository.SocketRepository
import kotlinx.coroutines.flow.Flow

class ConnectSocketUseCase(private val socketRepository: SocketRepository) {
    operator fun invoke(): Flow<Boolean> = socketRepository.connectToSocket()
}