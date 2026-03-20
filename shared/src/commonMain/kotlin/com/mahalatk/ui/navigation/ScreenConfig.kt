package com.mahalatk.ui.navigation

import com.mahalatk.common.component.toolbar.ToolBarState
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.more

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

    is Route.Register -> ScreenConfig(
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


}
