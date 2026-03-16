package com.aait.domain.repository

import kotlinx.coroutines.flow.Flow

interface PreferenceRepository {

    suspend fun getLanguage(): Flow<String>
    suspend fun setLanguage(lang: String)

    suspend fun getToken(): Flow<String>
    suspend fun setToken(mToken: String)

    suspend fun getNotifyStatus(): Flow<String>
    suspend fun setNotifyStatus(status: String)

    suspend fun getUserData(): Flow<String>
    suspend fun setUserData(userData: String)

    suspend fun getSocketData(): Flow<String>
    suspend fun setSocketData(data: String)

    suspend fun getFirebaseToken(): Flow<String>
    suspend fun setFirebaseToken(token: String)

    suspend fun getUserType(): Flow<String>
    suspend fun setUserType(type: String)

    suspend fun setAvailabilityStatus(status: Boolean)

    suspend fun getAvailabilityStatus(): Flow<Boolean>

    suspend fun getIsFirstTime(): Flow<Boolean>
    suspend fun setIsFirstTime(firstTime: Boolean)

    suspend fun getIsLogin(): Flow<Boolean>
    suspend fun setIsLogin(active: Boolean)

    suspend fun getCountries(): Flow<String>
    suspend fun setCountries(countries: String)

    suspend fun onLogout()
}