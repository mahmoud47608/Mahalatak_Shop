package com.aait.ui.util.paging

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.aait.domain.entity.base.BaseResponse
import com.aait.domain.util.NetworkExceptions
import com.aait.ui.util.NetworkExtensionsActions
import com.aait.ui.util.StringKeys

/**
 * Observes paging state changes and handles errors automatically.
 * Similar to how DataState.applyCommonSideEffects is called in ViewModel.
 *
 * Usage in Composable:
 * ```
 * val pagingItems = viewModel.items.collectAsLazyPagingItems()
 * pagingItems.ObservePagingState(viewModel)
 * ```
 */
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

/**
 * Handles paging load states internally.
 * Manages loading indicator and error handling for refresh, append, and prepend states.
 */
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

/**
 * Handles paging errors using the same pattern as applyCommonSideEffects error handling.
 */
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

/**
 * Checks if the paging data is empty (no items and not loading).
 */
fun <T : Any> LazyPagingItems<T>.isEmpty(): Boolean {
    return itemCount == 0 && loadState.refresh is LoadState.NotLoading
}

/**
 * Checks if the paging data has items.
 */
fun <T : Any> LazyPagingItems<T>.hasItems(): Boolean {
    return itemCount > 0
}

/**
 * Checks if currently showing initial loading state.
 */
fun <T : Any> LazyPagingItems<T>.isInitialLoading(): Boolean {
    return itemCount == 0 && loadState.refresh is LoadState.Loading
}

/**
 * Checks if currently loading more items (append loading).
 */
fun <T : Any> LazyPagingItems<T>.isLoadingMore(): Boolean {
    return loadState.append is LoadState.Loading
}

/**
 * Checks if the initial load failed.
 */
fun <T : Any> LazyPagingItems<T>.isRefreshError(): Boolean {
    return loadState.refresh is LoadState.Error
}

/**
 * Gets the error from refresh state if present.
 */
fun <T : Any> LazyPagingItems<T>.getRefreshError(): Throwable? {
    return (loadState.refresh as? LoadState.Error)?.error
}

/**
 * Checks if the end of pagination has been reached (no more pages to load).
 */
fun <T : Any> LazyPagingItems<T>.isEndOfList(): Boolean {
    val appendState = loadState.append
    return appendState is LoadState.NotLoading && appendState.endOfPaginationReached
}

/**
 * Checks if there's an error loading more items (append error).
 */
fun <T : Any> LazyPagingItems<T>.isAppendError(): Boolean {
    return loadState.append is LoadState.Error
}

/**
 * Gets the error from append state if present.
 */
fun <T : Any> LazyPagingItems<T>.getAppendError(): Throwable? {
    return (loadState.append as? LoadState.Error)?.error
}
