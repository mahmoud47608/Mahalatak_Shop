package com.mahalatk.features.packages

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.mahalatk.domain.entity.PackageData
import com.mahalatk.theme.AppColor
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.ic_nav_home
import mahalatk.shared.generated.resources.ic_nav_more
import mahalatk.shared.generated.resources.ic_orders
import mahalatk.shared.generated.resources.ic_rating
import mahalatk.shared.generated.resources.ic_send
import org.jetbrains.compose.resources.DrawableResource

enum class PackageStatus { AVAILABLE, SUBSCRIBED, PENDING }

@Immutable
data class PackageItem(
    val id: Int,
    val name: String,
    val description: String,
    val price: String,
    val type: String,
    val imageUrl: String,
    val status: PackageStatus,
)

@Immutable
data class PackagesState(
    val packages: ImmutableList<PackageItem> = persistentListOf(),
    val isLoading: Boolean = true,
)

@Immutable
data class PackageDetailState(
    val packageItem: PackageItem? = null,
    val isLoading: Boolean = false,
    val isSubscribing: Boolean = false,
    val showSubscribeSuccess: Boolean = false,
)

// ─── Type → Icon & Color mapping ───────────────

data class PackageVisuals(val icon: DrawableResource, val color: Color)

fun getPackageVisuals(type: String): PackageVisuals = when (type) {
    "messages" -> PackageVisuals(Res.drawable.ic_send, Color(0xFF5C6BC0))
    "home_slider" -> PackageVisuals(Res.drawable.ic_nav_home, Color(0xFF5EAAB0))
    "more_slider" -> PackageVisuals(Res.drawable.ic_nav_more, Color(0xFFCE9B58))
    "cart_slider" -> PackageVisuals(Res.drawable.ic_orders, Color(0xFF4CAF50))
    "most_popular" -> PackageVisuals(Res.drawable.ic_rating, Color(0xFFE91E63))
    else -> PackageVisuals(Res.drawable.ic_orders, AppColor.Primary)
}

// ─── Mapping ───────────────────────────────────

/** Temporary holder for passing PackageItem between screens. */
object PackageItemHolder {
    private var item: PackageItem? = null
    fun set(pkg: PackageItem) {
        item = pkg
    }

    fun get(): PackageItem? = item.also { item = null }
}

fun PackageData.toPackageItem() = PackageItem(
    id = id,
    name = name.orEmpty(),
    description = description.orEmpty(),
    price = "${price?.toInt() ?: 0}",
    type = type.orEmpty(),
    imageUrl = image.orEmpty(),
    status = when (status) {
        "subscribed" -> PackageStatus.SUBSCRIBED
        "pending" -> PackageStatus.PENDING
        else -> PackageStatus.AVAILABLE
    },
)
