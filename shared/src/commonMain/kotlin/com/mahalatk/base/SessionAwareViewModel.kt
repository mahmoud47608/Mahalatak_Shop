package com.mahalatk.base

import androidx.lifecycle.viewModelScope
import com.mahalatk.base.managers.LoadingManager
import com.mahalatk.base.managers.MessageManager
import com.mahalatk.base.managers.SessionManager
import com.mahalatk.domain.entity.base.BaseResponse
import com.mahalatk.domain.repository.PreferenceRepository
import com.mahalatk.domain.util.TokenCacheManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class SessionAwareViewModel<UiState : Any>(
    initialState: UiState,
    loadingManager: LoadingManager,
    messageManager: MessageManager,
    protected val sessionManager: SessionManager,
    protected val preferenceRepository: PreferenceRepository,
    protected val tokenCacheManager: TokenCacheManager,
) : BaseViewModel<UiState>(initialState, loadingManager, messageManager) {

    override fun authorizationNeedActive(msg: String, data: BaseResponse<*>) {
        msg.takeIf { it.isNotEmpty() }?.let {
            viewModelScope.launch { messageManager.show(it) }
        }
    }

    override fun authorizationFail() {
        viewModelScope.launch {
            logout()
            sessionManager.setAuthFailed(true)
        }
    }

    override fun block() {
        viewModelScope.launch {
            logout()
            sessionManager.setBlocked(true)
        }
    }

    private var fcmObserverJob: Job? = null

    fun observeFcmUpdates(orderId: Int?, onUpdate: () -> Unit) {
        fcmObserverJob?.cancel()
        fcmObserverJob = viewModelScope.launch {
            sessionManager.fcmUpdate.collectLatest { data ->
                val fcmOrderId = data["order_id"]?.toIntOrNull()
                if (fcmOrderId != null && fcmOrderId == orderId) {
                    withContext(Dispatchers.Main) {
                        onUpdate()
                    }
                }
            }
        }
    }

    open suspend fun logout() {
        preferenceRepository.setUserData("")
        preferenceRepository.setToken("")
        tokenCacheManager.removeToken()
        preferenceRepository.setIsLogin(false)
    }
}
