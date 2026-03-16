package com.aait.data.remote

import com.aait.domain.entity.chat.RoomMessagesResponse
import com.aait.domain.entity.chat.UploadMessageFileResponse
import com.aait.domain.entity.base.BaseResponse
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ChatEndPoint {

    @GET("get-room-messages/{id}")
    suspend fun messages(
        @Path("id") id: Int,
        @Query("page") page: Int
    ): BaseResponse<RoomMessagesResponse>

    @Multipart
    @POST("upload-room-file/{id}")
    suspend fun uploadMessageFile(
        @Path("id") id: Int,
        @Part file: MultipartBody.Part
    ): BaseResponse<UploadMessageFileResponse>
}