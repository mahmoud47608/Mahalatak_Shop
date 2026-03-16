package com.aait.domain.usecase.chat

import com.aait.domain.repository.ChatRepository
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UploadMessageFileUseCase @Inject constructor(private val chatRepository: ChatRepository) {
    operator fun invoke(id: Int, file: String, fileType: String) = flow {
        emitAll(
            chatRepository.uploadMessageFile(
                id = id,
                file = file,
                fileType = fileType
            )
        )
    }
}