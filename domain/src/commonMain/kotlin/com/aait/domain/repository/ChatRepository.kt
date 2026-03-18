package com.aait.domain.repository

import com.aait.domain.entity.base.BaseResponse
import com.aait.domain.entity.chat.RoomMessagesResponse
import com.aait.domain.entity.chat.UploadMessageFileResponse
import com.aait.domain.util.DataState
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    suspend fun messages(
        id: Int,
        page: Int
    ): Flow<DataState<BaseResponse<RoomMessagesResponse>>>

    suspend fun uploadMessageFile(
        id: Int,
        file: String,
        fileType: String
    ): Flow<DataState<BaseResponse<UploadMessageFileResponse>>>
}
