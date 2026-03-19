package com.aait.ui.navigation

import com.aait.common.component.toolbar.ToolBarState
import mahalatak.shared.generated.resources.Res
import mahalatak.shared.generated.resources.more

data class ScreenConfig(
    val toolBarState: ToolBarState = ToolBarState.Hidden,
    val hasTopPadding: Boolean = true,
    val hasBottomPadding: Boolean = true,
    val showBottomBar: Boolean = false,
)

fun Route.screenConfig(): ScreenConfig = when (this) {
    is Route.Splash -> ScreenConfig(
        hasTopPadding = false,
        hasBottomPadding = false,
    )

    is Route.Login -> ScreenConfig(
        hasTopPadding = false,
        hasBottomPadding = false,
    )

    is Route.Home -> ScreenConfig(
        hasTopPadding = false,
        hasBottomPadding = false,
        showBottomBar = true,
    )

    is Route.More -> ScreenConfig(
        toolBarState = ToolBarState.TitleWithBack(titleResId = Res.string.more),
        showBottomBar = true,
    )

    is Route.PickLocation -> ScreenConfig(
        hasTopPadding = false,
    )

    is Route.Chat -> ScreenConfig(
        toolBarState = ToolBarState.TitleWithBack(title = title ?: "Chat"),
    )
}
