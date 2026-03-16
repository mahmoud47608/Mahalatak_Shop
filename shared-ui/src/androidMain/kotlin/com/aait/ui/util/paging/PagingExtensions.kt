package com.aait.ui.util.paging

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.aait.ui.util.NetworkExtensionsActions
import com.aait.ui.util.StringKeys
import com.aait.domain.entity.base.BaseResponse
import com.aait.domain.util.NetworkExceptions

@Composable
fun <T : Any> LazyPagingItems<T>.ObservePagingState(
    networkExtensionsActions: NetworkExtensionsActions
) {
    LaunchedEffect(this.loadState) {
        this@ObservePagingState.loadState.handlePagingState(
            networkExtensionsActions = networkExtensionsActions
        )
    }
}

private fun CombinedLoadStates.handlePagingState(
    networkExtensionsActions: NetworkExtensionsActions
) {
    when (val refresh = this.refresh) {
        is LoadState.Loading -> {
            networkExtensionsActions.onLoad(true)
        }

        is LoadState.Error -> {
            networkExtensionsActions.onLoad(false)
            handlePagingError(refresh.error, networkExtensionsActions)
        }

        is LoadState.NotLoading -> {
            networkExtensionsActions.onLoad(false)
        }
    }

    when (val append = this.append) {
        is LoadState.Error -> {
            handlePagingError(append.error, networkExtensionsActions)
        }

        else -> {}
    }

    when (val prepend = this.prepend) {
        is LoadState.Error -> {
            handlePagingError(prepend.error, networkExtensionsActions)
        }

        else -> {}
    }
}

private fun handlePagingError(
    error: Throwable,
    networkExtensionsActions: NetworkExtensionsActions
) {
    when (error) {
        is NetworkExceptions.AuthorizationException -> {
            networkExtensionsActions.authorizationFail()
        }

        is NetworkExceptions.NeedActiveException -> {
            networkExtensionsActions.authorizationNeedActive(error.msg, BaseResponse<Any>())
        }

        is NetworkExceptions.ConnectionException -> {
            networkExtensionsActions.onCommonError(StringKeys.NO_INTERNET_CONNECTION)
        }

        is NetworkExceptions.CustomException -> {
            networkExtensionsActions.onFail(error.msg)
        }

        is NetworkExceptions.ServerException -> {
            networkExtensionsActions.onCommonError(StringKeys.SOMETHING_WENT_WRONG)
        }

        is NetworkExceptions.TimeoutException -> {
            networkExtensionsActions.onCommonError(StringKeys.NO_INTERNET_CONNECTION)
        }

        is NetworkExceptions.UnknownException -> {
            networkExtensionsActions.onCommonError(StringKeys.SOMETHING_WENT_WRONG)
        }

        else -> {
            networkExtensionsActions.onCommonError(StringKeys.SOMETHING_WENT_WRONG)
        }
    }
}

fun <T : Any> LazyPagingItems<T>.isEmpty(): Boolean {
    return itemCount == 0 && loadState.refresh is LoadState.NotLoading
}

fun <T : Any> LazyPagingItems<T>.hasItems(): Boolean {
    return itemCount > 0
}

fun <T : Any> LazyPagingItems<T>.isInitialLoading(): Boolean {
    return itemCount == 0 && loadState.refresh is LoadState.Loading
}

fun <T : Any> LazyPagingItems<T>.isLoadingMore(): Boolean {
    return loadState.append is LoadState.Loading
}

fun <T : Any> LazyPagingItems<T>.isRefreshError(): Boolean {
    return loadState.refresh is LoadState.Error
}

fun <T : Any> LazyPagingItems<T>.getRefreshError(): Throwable? {
    return (loadState.refresh as? LoadState.Error)?.error
}

fun <T : Any> LazyPagingItems<T>.isEndOfList(): Boolean {
    val appendState = loadState.append
    return appendState is LoadState.NotLoading && appendState.endOfPaginationReached
}

fun <T : Any> LazyPagingItems<T>.isAppendError(): Boolean {
    return loadState.append is LoadState.Error
}

fun <T : Any> LazyPagingItems<T>.getAppendError(): Throwable? {
    return (loadState.append as? LoadState.Error)?.error
}
