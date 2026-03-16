package com.aait.base.util.paging

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.mahalatak.R

/**
 * Adds a loading indicator item when appending more data.
 * Place this at the end of your LazyColumn items.
 *
 * Usage:
 * ```
 * LazyColumn {
 *     items(pagingItems.itemCount) { ... }
 *     pagingLoadingItem(pagingItems)
 * }
 * ```
 */
fun <T : Any> LazyListScope.pagingLoadingItem(
    pagingItems: LazyPagingItems<T>
) {
    if (pagingItems.loadState.append is LoadState.Loading) {
        item(key = "paging_loading") {
            PagingLoadingIndicator()
        }
    }
}

/**
 * Adds an error item with retry button when append fails.
 * Place this at the end of your LazyColumn items.
 *
 * Usage:
 * ```
 * LazyColumn {
 *     items(pagingItems.itemCount) { ... }
 *     pagingErrorItem(pagingItems) { pagingItems.retry() }
 * }
 * ```
 */
fun <T : Any> LazyListScope.pagingErrorItem(
    pagingItems: LazyPagingItems<T>,
    onRetry: () -> Unit
) {
    val appendState = pagingItems.loadState.append
    if (appendState is LoadState.Error) {
        item(key = "paging_error") {
            PagingErrorItem(
                message = appendState.error.localizedMessage,
                onRetry = onRetry
            )
        }
    }
}

/**
 * Adds both loading and error items for append states.
 * Convenience function combining pagingLoadingItem and pagingErrorItem.
 *
 * Usage:
 * ```
 * LazyColumn {
 *     items(pagingItems.itemCount) { ... }
 *     pagingAppendItems(pagingItems) { pagingItems.retry() }
 * }
 * ```
 */
fun <T : Any> LazyListScope.pagingAppendItems(
    pagingItems: LazyPagingItems<T>,
    onRetry: () -> Unit = { pagingItems.retry() }
) {
    pagingLoadingItem(pagingItems)
    pagingErrorItem(pagingItems, onRetry)
}

/**
 * Loading indicator composable for pagination.
 */
@Composable
fun PagingLoadingIndicator(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(32.dp),
            strokeWidth = 2.dp
        )
    }
}

/**
 * Error item composable with retry button for pagination.
 */
@Composable
fun PagingErrorItem(
    modifier: Modifier = Modifier,
    message: String? = null,
    onRetry: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = message ?: stringResource(R.string.something_went_wrong),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )
        TextButton(onClick = onRetry) {
            Text(text = stringResource(R.string.retry))
        }
    }
}

/**
 * Adds an end-of-list indicator when there's no more data to load.
 * Place this at the end of your LazyColumn items.
 *
 * Usage:
 * ```
 * LazyColumn {
 *     items(pagingItems.itemCount) { ... }
 *     pagingEndOfListItem(pagingItems)
 * }
 * ```
 */
fun <T : Any> LazyListScope.pagingEndOfListItem(
    pagingItems: LazyPagingItems<T>,
    message: String? = null
) {
    val appendState = pagingItems.loadState.append
    if (appendState is LoadState.NotLoading && appendState.endOfPaginationReached && pagingItems.itemCount > 0) {
        item(key = "paging_end_of_list") {
            PagingEndOfListIndicator(message = message ?: stringResource(R.string.end_of_list))
        }
    }
}

/**
 * Adds loading, error, and end-of-list items for append states.
 * Comprehensive convenience function for all pagination footer states.
 *
 * Usage:
 * ```
 * LazyColumn {
 *     items(pagingItems.itemCount) { ... }
 *     pagingFooterItems(pagingItems)
 * }
 * ```
 */
fun <T : Any> LazyListScope.pagingFooterItems(
    pagingItems: LazyPagingItems<T>,
    onRetry: () -> Unit = { pagingItems.retry() },
    showEndOfList: Boolean = true,
    endOfListMessage: String? = null
) {
    pagingLoadingItem(pagingItems)
    pagingErrorItem(pagingItems, onRetry)
    if (showEndOfList) {
        pagingEndOfListItem(pagingItems, endOfListMessage)
    }
}

/**
 * End of list indicator composable.
 */
@Composable
fun PagingEndOfListIndicator(
    modifier: Modifier = Modifier,
    message: String = stringResource(R.string.end_of_list)
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
