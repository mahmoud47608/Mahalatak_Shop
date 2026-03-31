package com.mahalatk.features.ratings

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class Rating(
    val id: String,
    val customerName: String,
    val customerImage: String = "",
    val rating: Float,
    val comment: String,
    val date: String,
)

data class MyRatingsState(
    val ratings: List<Rating> = emptyList(),
    val averageRating: Float = 0f,
    val totalRatings: Int = 0,
)

class MyRatingsViewModel : ViewModel() {

    private val sampleRatings = listOf(
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

    private val avg = sampleRatings.map { it.rating }.average().toFloat()

    private val _uiState = MutableStateFlow(
        MyRatingsState(
            ratings = sampleRatings,
            averageRating = avg,
            totalRatings = sampleRatings.size,
        ),
    )
    val uiState: StateFlow<MyRatingsState> = _uiState.asStateFlow()
}
