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

fun <T> DataState<BaseResponse<T>>.applyCommonSideEffects(
    actions: NetworkExtensionsActions,
    showLoading: Boolean = true,
    showSuccessToast: Boolean = false,
    cancelNotActive: Boolean = false,
    onSuccess: (BaseResponse<T>) -> Unit = {},
) {
    when (this) {
        is DataState.Loading -> {
            if (showLoading) actions.onLoad(true)
        }

        is DataState.Success -> {
            actions.onLoad(false)
            val response = data
            val msg = response.msg
            when (response.key) {
                SUCCESS, ACTIVE -> {
                    if (showSuccessToast) actions.onShowSuccessToast(msg)
                    onSuccess(response)
                }

                NEED_ACTIVATE -> {
                    actions.authorizationNeedActive(msg, response)
                }

                UN_AUTH -> {
                    actions.authorizationFail()
                }

                BLOCK -> {
                    actions.block()
                }

                NOT_ACTIVE, PENDING, FAILED, EXCEPTION -> {
                    if (response.key == NOT_ACTIVE && cancelNotActive) {
                        if (showSuccessToast) actions.onShowSuccessToast(msg)
                        onSuccess(response)
                    } else {
                        actions.onFail(msg)
                    }
                }

                else -> {
                    actions.onCommonError(StringKeys.SOMETHING_WENT_WRONG)
                }
            }
        }

        is DataState.Error -> {
            actions.onLoad(false)
            handleError(actions, throwable, this.data)
        }

        DataState.Idle -> {
            actions.onLoad(false)
        }
    }
}

fun handleError(
    actions: NetworkExtensionsActions,
    throwable: Throwable,
    data: BaseResponse<*>?
) {
    when (throwable) {
        is NetworkExceptions.AuthorizationException -> {
            actions.authorizationFail()
        }

        is NetworkExceptions.NeedActiveException -> {
            val response = data
            if (response != null) {
                actions.authorizationNeedActive(throwable.msg, response)
            } else {
                actions.onFail(throwable.msg)
            }
        }

        is NetworkExceptions.ConnectionException -> {
            actions.onCommonError(StringKeys.NO_INTERNET_CONNECTION)
        }

        is NetworkExceptions.CustomException -> {
            actions.onFail(throwable.msg)
        }

        else -> {
            actions.onCommonError(throwable.getIsCommonException())
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
