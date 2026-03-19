@file:OptIn(InternalResourceApi::class)

package mahalatak.shared.generated.resources

import kotlin.OptIn
import kotlin.String
import kotlin.collections.MutableMap
import org.jetbrains.compose.resources.FontResource
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.ResourceItem

private const val MD: String = "composeResources/mahalatak.shared.generated.resources/"

internal val Res.font.expo_arabic_bold: FontResource by lazy {
      FontResource("font:expo_arabic_bold", setOf(
        ResourceItem(setOf(), "${MD}font/expo_arabic_bold.ttf", -1, -1),
      ))
    }

internal val Res.font.expo_arabic_book: FontResource by lazy {
      FontResource("font:expo_arabic_book", setOf(
        ResourceItem(setOf(), "${MD}font/expo_arabic_book.ttf", -1, -1),
      ))
    }

internal val Res.font.expo_arabic_light: FontResource by lazy {
      FontResource("font:expo_arabic_light", setOf(
        ResourceItem(setOf(), "${MD}font/expo_arabic_light.ttf", -1, -1),
      ))
    }

internal val Res.font.expo_arabic_medium: FontResource by lazy {
      FontResource("font:expo_arabic_medium", setOf(
        ResourceItem(setOf(), "${MD}font/expo_arabic_medium.ttf", -1, -1),
      ))
    }

internal val Res.font.expo_arabic_semibold: FontResource by lazy {
      FontResource("font:expo_arabic_semibold", setOf(
        ResourceItem(setOf(), "${MD}font/expo_arabic_semibold.ttf", -1, -1),
      ))
    }

@InternalResourceApi
internal fun _collectCommonMainFont0Resources(map: MutableMap<String, FontResource>) {
  map.put("expo_arabic_bold", Res.font.expo_arabic_bold)
  map.put("expo_arabic_book", Res.font.expo_arabic_book)
  map.put("expo_arabic_light", Res.font.expo_arabic_light)
  map.put("expo_arabic_medium", Res.font.expo_arabic_medium)
  map.put("expo_arabic_semibold", Res.font.expo_arabic_semibold)
}
