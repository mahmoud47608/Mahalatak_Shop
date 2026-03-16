package com.aait.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aait.domain.entity.base.BaseResponse
import com.aait.domain.util.TokenManager
import com.aait.domain.repository.PreferenceRepository
import com.aait.ui.ui.UIRepo
import com.aait.ui.util.NetworkExtensionsActions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

open class BaseViewModel<UiState : Any>(
    initialState: UiState,
    protected val uiRepo: UIRepo,
    protected val preferenceRepository: PreferenceRepository,
    protected val tokenManager: TokenManager
) : ViewModel(), NetworkExtensionsActions {

    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun updateState(reducer: UiState.() -> UiState) {
        _uiState.update { it.reducer() }
    }

    override fun onLoad(showLoading: Boolean) {
        if (showLoading) uiRepo.showLoading()
        else uiRepo.hideLoading()
    }

    override fun onCommonError(errorKey: String) {
        onLoad(false)
        viewModelScope.launch { uiRepo.showMsg(errorKey, isKey = true) }
    }

    override fun onShowSuccessToast(msg: String?) {
        msg?.takeIf { it.isNotEmpty() }?.let { viewModelScope.launch { uiRepo.showMsg(it) } }
    }

    override fun onFail(msg: String?) {
        msg?.takeIf { it.isNotEmpty() }?.let { viewModelScope.launch { uiRepo.showMsg(it) } }
    }

    override fun authorizationNeedActive(msg: String, data: BaseResponse<*>) {
        msg.takeIf { it.isNotEmpty() }?.let { viewModelScope.launch { uiRepo.showMsg(it) } }
    }

    override fun authorizationFail() {
        viewModelScope.launch {
            logout()
            uiRepo.setAuthFailed(true)
        }
    }

    override fun block() {
        viewModelScope.launch {
            logout()
            uiRepo.setBlocked(true)
        }
    }

    fun observeFcmUpdates(orderId: Int?, onUpdate: () -> Unit) {
        viewModelScope.launch {
            uiRepo.fcmUpdate.collect { data ->
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
        tokenManager.removeToken()
        preferenceRepository.setIsLogin(false)
    }
}
