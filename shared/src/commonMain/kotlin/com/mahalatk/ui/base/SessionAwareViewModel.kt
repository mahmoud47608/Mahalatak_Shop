package com.mahalatk.ui.base

import androidx.lifecycle.viewModelScope
import com.mahalatk.domain.entity.base.BaseResponse
import com.mahalatk.domain.repository.PreferenceRepository
import com.mahalatk.domain.util.TokenCacheManager
import com.mahalatk.ui.managers.LoadingManager
import com.mahalatk.ui.managers.MessageManager
import com.mahalatk.ui.managers.SessionManager
import kotlinx.coroutines.launch

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

    fun observeFcmUpdates(orderId: Int?, onUpdate: () -> Unit) {
        viewModelScope.launch {
            sessionManager.fcmUpdate.collect { data ->
                val fcmOrderId = data["order_id"]?.toIntOrNull()
                if (fcmOrderId != null && fcmOrderId == orderId) {
                    onUpdate()
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
