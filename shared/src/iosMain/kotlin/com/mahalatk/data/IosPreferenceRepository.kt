package com.mahalatk.data

import com.mahalatk.domain.repository.PreferenceRepository
import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class IosPreferenceRepository : PreferenceRepository {

    private val settings = Settings()

    override suspend fun getLanguage(): Flow<String> =
        flowOf(settings.getString("language", "en"))

    override suspend fun setLanguage(lang: String) {
        settings.putString("language", lang)
    }

    override suspend fun getToken(): Flow<String> =
        flowOf(settings.getString("token", ""))

    override suspend fun setToken(mToken: String) {
        settings.putString("token", mToken)
    }

    override suspend fun getNotifyStatus(): Flow<String> =
        flowOf(settings.getString("notify_status", ""))

    override suspend fun setNotifyStatus(status: String) {
        settings.putString("notify_status", status)
    }

    override suspend fun getUserData(): Flow<String> =
        flowOf(settings.getString("user_data", ""))

    override suspend fun setUserData(userData: String) {
        settings.putString("user_data", userData)
    }

    override suspend fun getSocketData(): Flow<String> =
        flowOf(settings.getString("socket_data", ""))

    override suspend fun setSocketData(data: String) {
        settings.putString("socket_data", data)
    }

    override suspend fun getFirebaseToken(): Flow<String> =
        flowOf(settings.getString("firebase_token", ""))

    override suspend fun setFirebaseToken(token: String) {
        settings.putString("firebase_token", token)
    }

    override suspend fun getUserType(): Flow<String> =
        flowOf(settings.getString("user_type", ""))

    override suspend fun setUserType(type: String) {
        settings.putString("user_type", type)
    }

    override suspend fun setAvailabilityStatus(status: Boolean) {
        settings.putBoolean("availability_status", status)
    }

    override suspend fun getAvailabilityStatus(): Flow<Boolean> =
        flowOf(settings.getBoolean("availability_status", false))

    override suspend fun getIsFirstTime(): Flow<Boolean> =
        flowOf(settings.getBoolean("is_first_time", true))

    override suspend fun setIsFirstTime(firstTime: Boolean) {
        settings.putBoolean("is_first_time", firstTime)
    }

    override suspend fun getIsLogin(): Flow<Boolean> =
        flowOf(settings.getBoolean("is_login", false))

    override suspend fun setIsLogin(active: Boolean) {
        settings.putBoolean("is_login", active)
    }

    override suspend fun getCountries(): Flow<String> =
        flowOf(settings.getString("countries", ""))

    override suspend fun setCountries(countries: String) {
        settings.putString("countries", countries)
    }

    override suspend fun onLogout() {
        settings.remove("token")
        settings.remove("is_login")
        settings.remove("user_data")
    }
}
