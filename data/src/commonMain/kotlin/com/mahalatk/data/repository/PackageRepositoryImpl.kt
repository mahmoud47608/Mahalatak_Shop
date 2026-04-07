package com.mahalatk.data.repository

import com.mahalatk.data.remote.PackageEndPoint
import com.mahalatk.data.util.safeApiCall
import com.mahalatk.domain.repository.PackageRepository

class PackageRepositoryImpl(private val endPoint: PackageEndPoint) : PackageRepository {

    override suspend fun getPackages() = safeApiCall {
        endPoint.getPackages()
    }

    override suspend fun subscribeToPackage(packageId: Int) = safeApiCall {
        endPoint.subscribeToPackage(packageId)
    }
}
