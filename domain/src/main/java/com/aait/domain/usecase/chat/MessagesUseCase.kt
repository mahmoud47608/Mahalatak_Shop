package com.aait.domain.usecase.chat

import com.aait.domain.repository.ChatRepository
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MessagesUseCase @Inject constructor(private val chatRepository: ChatRepository) {
    operator fun invoke(id: Int, page: Int) = flow {
        emitAll(
            chatRepository.messages(
                id = id,
                page = page
            )
        )
    }
}