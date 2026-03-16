package com.aait.data.repo_impl

import com.aait.data.remote.ChatEndPoint
import com.aait.data.util.multipart.MultiPartUtil.toMultiPart
import com.aait.data.util.safeApiCall
import com.aait.domain.repository.ChatRepository
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(private val chatEndPoint: ChatEndPoint) :
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
            file = file.toMultiPart("file")
        )
    }
}