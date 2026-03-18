package com.aait.data.repo_impl

import com.aait.data.remote.ChatEndPoint
import com.aait.data.util.safeApiCall
import com.aait.domain.repository.ChatRepository

class ChatRepositoryImpl(private val chatEndPoint: ChatEndPoint) :
    ChatRepository {

    override suspend fun messages(
        id: Int,
        page: Int
    ) = safeApiCall {
        chatEndPoint.messages(
            id = id,
            page = page
        )
    }

    override suspend fun uploadMessageFile(
        id: Int,
        file: String,
        fileType: String
    ) = safeApiCall {
        chatEndPoint.uploadMessageFile(
            id = id,
            filePath = file,
            fileName = file.substringAfterLast("/"),
            contentType = fileType
        )
    }
}
