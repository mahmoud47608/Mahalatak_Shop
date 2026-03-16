package com.aait.base.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aait.base.R
import com.aait.base.common.component.toolbar.ToolBarState
import com.aait.base.di.BaseViewModelEntryPoint
import com.aait.base.ui.UIRepo
import com.aait.base.util.ApplicationContextHolder
import com.aait.base.util.NetworkExtensionsActions
import com.aait.data.util.TokenHeaderProvider
import com.aait.domain.entity.base.BaseResponse
import com.aait.domain.repository.PreferenceRepository
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

open class BaseViewModel<UiState : Any>(
    initialState: UiState
) : ViewModel(), NetworkExtensionsActions {

    private val entryPoint: BaseViewModelEntryPoint by lazy(LazyThreadSafetyMode.NONE) {
        EntryPointAccessors.fromApplication(
            ApplicationContextHolder.application,
            BaseViewModelEntryPoint::class.java
        )
    }

    protected val uiRepo: UIRepo by lazy(LazyThreadSafetyMode.NONE) {
        entryPoint.uiRepo()
    }

    protected val preferenceRepository: PreferenceRepository by lazy(LazyThreadSafetyMode.NONE) {
        entryPoint.preferenceRepository()
    }

    protected val tokenHeaderProvider: TokenHeaderProvider by lazy(LazyThreadSafetyMode.NONE) {
        entryPoint.tokenHeaderProvider()
    }

    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun updateState(reducer: UiState.() -> UiState) {
        _uiState.update { it.reducer() }
    }

    override fun onLoad(showLoading: Boolean) {
        if (showLoading) uiRepo.showLoading()
        else uiRepo.hideLoading()
    }

    override fun onCommonError(exceptionMsgId: Int) {
        onLoad(false)
        viewModelScope.launch { uiRepo.showMsg(exceptionMsgId) }
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

    fun getFirebaseToken(action: (String) -> Unit) {
        viewModelScope.launch {
            val token = preferenceRepository.getFirebaseToken().first()
            if (token.isEmpty()) getFirebaseTokenFromServer(action)
            else action.invoke(token)
        }
    }

    private fun getFirebaseTokenFromServer(action: (String) -> Unit) {
        onLoad(true)
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            viewModelScope.launch {
                if (it.isSuccessful) {
                    preferenceRepository.setFirebaseToken(it.result)
                    action(it.result)
                } else {
                    uiRepo.showMsg(R.string.no_internet_connection)
                }
            }
            onLoad(false)
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
        tokenHeaderProvider.removeToken()
        preferenceRepository.setIsLogin(false)
    }
}
