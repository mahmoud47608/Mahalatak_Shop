package com.aait.base.ui.navigation

import androidx.navigation3.runtime.NavKey
import com.aait.base.common.component.toolbar.ToolBarState
import com.aait.domain.entity.general.LatLngModel
import com.mahalatak.R
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
sealed class NavScreen(
    @Transient open val toolBarState: ToolBarState = ToolBarState.Hidden,
    @Transient open val hasTopPadding: Boolean = true,
    @Transient open val hasBottomPadding: Boolean = true,
) : NavKey

val bottomBarItems = listOf(
    BottomAppBarModel.Home(), BottomAppBarModel.More()
)

@Serializable
object SplashNavKey : NavScreen()

@Serializable
object LoginNavKey : NavScreen()

@Serializable
object HomeNavKey : NavScreen()

@Serializable
data class MoreNavKey(
    @Transient override val toolBarState: ToolBarState = ToolBarState.TitleWithBack(
        titleResId = R.string.more
    )
) : NavScreen()

@Serializable
data class PickLocationNavKey(
    val latLngModel: LatLngModel?, override val hasTopPadding: Boolean = false
) : NavScreen()

ء ~@Serializable
data class ChatNavKey(
    val roomId: Int,
    val title: String? = null
) : NavScreen()

