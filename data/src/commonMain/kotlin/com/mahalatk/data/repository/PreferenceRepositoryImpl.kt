package com.mahalatk.data.repository

import com.mahalatk.data.util.PreferenceConstants
import com.mahalatk.domain.datasource.PreferenceDataSource
import com.mahalatk.domain.repository.PreferenceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferenceRepositoryImpl(
    private val dataSource: PreferenceDataSource,
) : PreferenceRepository {

    // ─── String preferences ─────────────────

    override suspend fun getLanguage(): Flow<String> =
        getString(PreferenceConstants.LANGUAGE, "en")

    override suspend fun setLanguage(lang: String) =
        dataSource.setValue(PreferenceConstants.LANGUAGE, lang)

    override suspend fun getToken(): Flow<String> =
        getString(PreferenceConstants.TOKEN, "")

    override suspend fun setToken(mToken: String) =
        dataSource.setValue(PreferenceConstants.TOKEN, mToken)

    override suspend fun getNotifyStatus(): Flow<String> =
        getString(PreferenceConstants.NOTIFY_STATUS, "true")

    override suspend fun setNotifyStatus(status: String) =
        dataSource.setValue(PreferenceConstants.NOTIFY_STATUS, status)

    override suspend fun getUserData(): Flow<String> =
        getString(PreferenceConstants.USER_DATA, "")

    override suspend fun setUserData(userData: String) =
        dataSource.setValue(PreferenceConstants.USER_DATA, userData)

    override suspend fun getSocketData(): Flow<String> =
        getString(PreferenceConstants.SOCKET_DATA, "")

    override suspend fun setSocketData(data: String) =
        dataSource.setValue(PreferenceConstants.SOCKET_DATA, data)

    override suspend fun getFirebaseToken(): Flow<String> =
        getString(PreferenceConstants.FIREBASE_TOKEN, "")

    override suspend fun setFirebaseToken(token: String) =
        dataSource.setValue(PreferenceConstants.FIREBASE_TOKEN, token)

    override suspend fun getUserType(): Flow<String> =
        getString(PreferenceConstants.USER_TYPE, "user")

    override suspend fun setUserType(type: String) =
        dataSource.setValue(PreferenceConstants.USER_TYPE, type)

    override suspend fun getCountries(): Flow<String> =
        getString(PreferenceConstants.COUNTRIES, "")

    override suspend fun setCountries(countries: String) =
        dataSource.setValue(PreferenceConstants.COUNTRIES, countries)

    // ─── Boolean preferences ────────────────

    override suspend fun getIsFirstTime(): Flow<Boolean> =
        getBoolean(PreferenceConstants.IS_FIRST_TIME, true)

    override suspend fun setIsFirstTime(firstTime: Boolean) =
        dataSource.setValue(PreferenceConstants.IS_FIRST_TIME, firstTime)

    override suspend fun getIsLogin(): Flow<Boolean> =
        getBoolean(PreferenceConstants.IS_LOGIN, false)

    override suspend fun setIsLogin(active: Boolean) =
        dataSource.setValue(PreferenceConstants.IS_LOGIN, active)

    override suspend fun getAvailabilityStatus(): Flow<Boolean> =
        getBoolean(PreferenceConstants.AVAILABILITY, true)

    override suspend fun setAvailabilityStatus(status: Boolean) =
        dataSource.setValue(PreferenceConstants.AVAILABILITY, status)

    override suspend fun getDarkMode(): Flow<Boolean> =
        getBoolean(PreferenceConstants.DARK_MODE, false)

    override suspend fun setDarkMode(isDark: Boolean) =
        dataSource.setValue(PreferenceConstants.DARK_MODE, isDark)

    // ─── Logout ─────────────────────────────

    override suspend fun onLogout() {
        setToken("")
        setUserData("")
        setSocketData("")
        setIsLogin(false)
    }

    // ─── Helpers (type-safe, no unsafe casts) ──

    private suspend fun getString(key: String, default: String): Flow<String> =
        dataSource.getValue(key, default).map { it as? String ?: default }

    private suspend fun getBoolean(key: String, default: Boolean): Flow<Boolean> =
        dataSource.getValue(key, default).map { it as? Boolean ?: default }
}
