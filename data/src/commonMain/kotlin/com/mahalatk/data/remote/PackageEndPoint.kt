package com.mahalatk.data.remote

import com.mahalatk.domain.entity.PackageData
import com.mahalatk.domain.entity.base.BaseResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.statement.readRawBytes
import kotlinx.serialization.json.Json

class PackageEndPoint(private val client: HttpClient, private val json: Json) {

    suspend fun getPackages(): BaseResponse<List<PackageData>> =
        json.decodeFromString(client.get("packages").readRawBytes().decodeToString())

    suspend fun subscribeToPackage(packageId: Int): BaseResponse<Any> =
        json.decodeFromString(
            client.post("packages/$packageId/subscribe").readRawBytes().decodeToString()
        )
}
