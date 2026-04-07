package com.mahalatk.domain.repository

import com.mahalatk.domain.entity.PackageData
import com.mahalatk.domain.entity.base.BaseResponse
import com.mahalatk.domain.util.DataState
import kotlinx.coroutines.flow.Flow

interface PackageRepository {

    suspend fun getPackages(): Flow<DataState<BaseResponse<List<PackageData>>>>

    suspend fun subscribeToPackage(packageId: Int): Flow<DataState<BaseResponse<Any>>>
}
