package com.aait.data.remote

import com.aait.domain.entity.base.BaseResponse
import com.aait.domain.entity.chat.RoomMessagesResponse
import com.aait.domain.entity.chat.UploadMessageFileResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders

class ChatEndPoint(private val client: HttpClient) {

    suspend fun messages(
        id: Int,
        page: Int
    ): BaseResponse<RoomMessagesResponse> =
        client.get("get-room-messages/$id") {
            parameter("page", page)
        }.body()

    suspend fun uploadMessageFile(
        id: Int,
        filePath: String,
        fileName: String,
        contentType: String
    ): BaseResponse<UploadMessageFileResponse> =
        client.submitFormWithBinaryData(
            url = "upload-room-file/$id",
            formData = formData {
                append("file", filePath.encodeToByteArray(), Headers.build {
                    append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                    append(HttpHeaders.ContentType, contentType)
                })
            }
        ).body()
}
