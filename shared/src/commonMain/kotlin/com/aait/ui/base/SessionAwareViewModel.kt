package com.aait.ui.base

import androidx.lifecycle.viewModelScope
import com.aait.domain.entity.base.BaseResponse
import com.aait.domain.repository.PreferenceRepository
import com.aait.domain.util.TokenCacheManager
import com.aait.ui.managers.LoadingManager
import com.aait.ui.managers.MessageManager
import com.aait.ui.managers.SessionManager
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
