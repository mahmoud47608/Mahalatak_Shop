package com.aait.ui.component.toolbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.aait.ui.component.text.DefaultText


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultAppBar(
    state: ToolBarState,
    modifier: Modifier = Modifier,
) {
    if (state is ToolBarState.Hidden) return

    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            DefaultText(
                text = state.resolvedTitle(),
                textAlign = TextAlign.Center,
                textStyle = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                ),
                textColor = MaterialTheme.colorScheme.onSurface
            )
        },
        navigationIcon = {
            if (state.hasBackButton) {
                IconAction(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    onClick = { state.onBackButtonClicked?.invoke() }
                )
            }
        },
        actions = {
            when (state) {
                is ToolBarState.TitleWithNotification -> {
                    IconAction(
                        imageVector = Icons.Filled.Notifications,
                        contentDescription = "Notifications",
                        onClick = { state.onNotificationButtonClicked?.invoke() }
                    )
                }

                is ToolBarState.TitleWithBackAndActions -> {
                    state.actions.forEach { action -> action() }
                }

                else -> Unit
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = if (state is ToolBarState.AuthTitleWithBack)
                MaterialTheme.colorScheme.background
            else
                MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurface
        )
    )

}

@Composable
private fun ToolBarState.resolvedTitle(): String {
    return when (this) {
        is ToolBarState.TitleWithBack ->
            titleKey ?: title.orEmpty()

        is ToolBarState.AuthTitleWithBack ->
            titleKey ?: title.orEmpty()

        else -> title.orEmpty()
    }
}

private val ToolBarState.hasBackButton: Boolean
    get() = this is ToolBarState.TitleWithBack ||
            this is ToolBarState.AuthTitleWithBack ||
            this is ToolBarState.TitleWithBackAndActions

@Composable
private fun IconAction(
    imageVector: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription
        )
    }
}

sealed interface ToolbarCommonState {
    val title: String?
    val onBackButtonClicked: (() -> Unit)?
    val onNotificationButtonClicked: (() -> Unit)?
}

@Immutable
sealed class ToolBarState(
    override val title: String? = null,
    val _onBackButtonClicked: (() -> Unit)? = null,
    override val onNotificationButtonClicked: (() -> Unit)? = null,
) : ToolbarCommonState {
    override val onBackButtonClicked: (() -> Unit)? = _onBackButtonClicked

    fun updateBackButtonClicked(newOnBackButtonClicked: () -> Unit): ToolBarState {
        return when (this) {
            is TitleWithBack -> copy(onBackButtonClicked = newOnBackButtonClicked)
            is AuthTitleWithBack -> copy(onBackButtonClicked = newOnBackButtonClicked)
            is TitleWithBackAndActions -> copy(onBackButtonClicked = newOnBackButtonClicked)
            else -> this // Hidden state doesn't need back button updates
        }
    }

    data object Hidden : ToolBarState()

    data class Title(
        override val title: String
    ) : ToolBarState()

    data class TitleWithBack(
        override val title: String? = null,
        val titleKey: String? = null,
        override val onBackButtonClicked: (() -> Unit)? = null,
    ) : ToolBarState()

    data class AuthTitleWithBack(
        override val title: String? = null,
        val titleKey: String? = null,
        override val onBackButtonClicked: (() -> Unit)? = null,
    ) : ToolBarState()

    data class TitleWithNotification(
        override val title: String,
        override val onNotificationButtonClicked: (() -> Unit)? = null,
    ) : ToolBarState()

    data class TitleWithBackAndActions(
        override val title: String,
        override val onBackButtonClicked: (() -> Unit)? = null,
        val actions: List<@Composable () -> Unit>,
    ) : ToolBarState()
}
