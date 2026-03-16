package com.aait.ui.util

import com.aait.domain.entity.base.BaseResponse
import com.aait.domain.util.DataState
import com.aait.domain.util.NetworkExceptions
import com.aait.domain.util.ResponseStatus.ACTIVE
import com.aait.domain.util.ResponseStatus.BLOCK
import com.aait.domain.util.ResponseStatus.EXCEPTION
import com.aait.domain.util.ResponseStatus.FAILED
import com.aait.domain.util.ResponseStatus.NEED_ACTIVATE
import com.aait.domain.util.ResponseStatus.NOT_ACTIVE
import com.aait.domain.util.ResponseStatus.PENDING
import com.aait.domain.util.ResponseStatus.SUCCESS
import com.aait.domain.util.ResponseStatus.UN_AUTH

interface NetworkExtensionsActions {
    fun onLoad(showLoading: Boolean) {}
    fun onCommonError(errorKey: String) {}
    fun onShowSuccessToast(msg: String?) {}
    fun onFail(msg: String?) {}
    fun authorizationFail() {}
    fun block() {}
    fun authorizationNeedActive(msg: String, data: BaseResponse<*>) {}
}

fun <T> DataState<T>.applyCommonSideEffects(
    networkExtensionsActions: NetworkExtensionsActions,
    showLoading: Boolean = true,
    showSuccessToast: Boolean = false,
    cancelNotActive: Boolean = false,
    onSuccess: (T) -> Unit = {},
) {
    when (this) {
        is DataState.Loading -> {
            if (showLoading) networkExtensionsActions.onLoad(true)
        }

        is DataState.Success -> {
            networkExtensionsActions.onLoad(false)
            val msg = (data as BaseResponse<*>).msg
            when ((data as BaseResponse<*>).key) {
                SUCCESS, ACTIVE -> {
                    if (showSuccessToast) networkExtensionsActions.onShowSuccessToast(msg)
                    onSuccess(this.data)
                }

                NEED_ACTIVATE -> {
                    networkExtensionsActions.authorizationNeedActive(
                        msg,
                        this.data as BaseResponse<*>
                    )
                }

                UN_AUTH -> {
                    networkExtensionsActions.authorizationFail()
                }

                BLOCK -> {
                    networkExtensionsActions.block()
                }

                NOT_ACTIVE, PENDING, FAILED, EXCEPTION -> {
                    if ((data as BaseResponse<*>).key == NOT_ACTIVE && cancelNotActive) {
                        if (showSuccessToast) networkExtensionsActions.onShowSuccessToast(msg)
                        onSuccess(this.data)
                    } else {
                        networkExtensionsActions.onFail(msg)
                    }
                }

                else -> {
                    networkExtensionsActions.onCommonError(StringKeys.SOMETHING_WENT_WRONG)
                }
            }
        }

        is DataState.Error -> {
            networkExtensionsActions.onLoad(false)
            handleError(networkExtensionsActions, throwable, this.data)
        }

        DataState.Idle -> {
            networkExtensionsActions.onLoad(false)
        }
    }
}

fun handleError(
    networkExtensionsActions: NetworkExtensionsActions,
    throwable: Throwable,
    data: Any?
) {
    when (throwable) {
        is NetworkExceptions.AuthorizationException -> {
            networkExtensionsActions.authorizationFail()
        }

        is NetworkExceptions.NeedActiveException -> {
            networkExtensionsActions.authorizationNeedActive(throwable.msg, data as BaseResponse<*>)
        }

        is NetworkExceptions.ConnectionException -> {
            networkExtensionsActions.onCommonError(StringKeys.NO_INTERNET_CONNECTION)
        }

        is NetworkExceptions.CustomException -> {
            networkExtensionsActions.onFail(throwable.msg)
        }

        else -> {
            networkExtensionsActions.onCommonError(throwable.getIsCommonException())
        }
    }
}

fun Throwable.getIsCommonException(): String {
    return when (this) {
        is NetworkExceptions.ConnectionException -> StringKeys.NO_INTERNET_CONNECTION
        is NetworkExceptions.TimeoutException -> StringKeys.NO_INTERNET_CONNECTION
        else -> StringKeys.SOMETHING_WENT_WRONG
    }
}
