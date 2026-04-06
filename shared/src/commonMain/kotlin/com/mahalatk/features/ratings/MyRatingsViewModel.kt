package com.mahalatk.features.ratings

import androidx.compose.runtime.Immutable
import com.mahalatk.base.SimpleViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class Rating(
    val id: String,
    val customerName: String,
    val customerImage: String = "",
    val rating: Float,
    val comment: String,
    val date: String,
)

@Immutable
data class MyRatingsState(
    val ratings: ImmutableList<Rating> = persistentListOf(),
    val averageRating: Float = 0f,
    val totalRatings: Int = 0,
)

class MyRatingsViewModel : SimpleViewModel<MyRatingsState, Nothing>(
    run {
        val sampleRatings = persistentListOf(
        Rating(
            id = "1",
            customerName = "فهد فرج",
            rating = 4.5f,
            comment = "بوكيه ورد مُختار بعناية ليجمع بين الجمال والرائحة العطرة، التعامل ممتاز وخدمة بشكل احترافي",
            date = "منذ يومين",
        ),
        Rating(
            id = "2",
            customerName = "سارة أحمد",
            rating = 5.0f,
            comment = "خدمة ممتازة وتوصيل سريع، المنتجات بجودة عالية جداً وأسعار مناسبة",
            date = "منذ 3 أيام",
        ),
        Rating(
            id = "3",
            customerName = "محمد خالد",
            rating = 3.5f,
            comment = "المنتجات جيدة بشكل عام، لكن التوصيل تأخر قليلاً عن الموعد المتوقع",
            date = "منذ أسبوع",
        ),
        Rating(
            id = "4",
            customerName = "نورة عبدالله",
            rating = 4.0f,
            comment = "تجربة تسوق رائعة، التغليف أنيق والمنتجات طازجة",
            date = "منذ أسبوعين",
        ),
        Rating(
            id = "5",
            customerName = "عمر حسن",
            rating = 5.0f,
            comment = "أفضل محل تعاملت معه، خدمة عملاء ممتازة ومنتجات فاخرة",
            date = "منذ شهر",
        ),
        )
        MyRatingsState(
            ratings = sampleRatings,
            averageRating = sampleRatings.map { it.rating }.average().toFloat(),
            totalRatings = sampleRatings.size,
        )
    },
)
