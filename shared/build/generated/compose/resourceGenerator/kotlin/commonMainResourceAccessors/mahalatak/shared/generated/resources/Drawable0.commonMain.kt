@file:OptIn(InternalResourceApi::class)

package mahalatak.shared.generated.resources

import kotlin.OptIn
import kotlin.String
import kotlin.collections.MutableMap
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.ResourceItem

private const val MD: String = "composeResources/mahalatak.shared.generated.resources/"

internal val Res.drawable.app_icon: DrawableResource by lazy {
      DrawableResource("drawable:app_icon", setOf(
        ResourceItem(setOf(), "${MD}drawable/app_icon.png", -1, -1),
      ))
    }

@InternalResourceApi
internal fun _collectCommonMainDrawable0Resources(map: MutableMap<String, DrawableResource>) {
  map.put("app_icon", Res.drawable.app_icon)
}
