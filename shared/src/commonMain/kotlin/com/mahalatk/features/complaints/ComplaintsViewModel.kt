package com.mahalatk.features.complaints

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class Complaint(
    val id: String,
    val userName: String,
    val userImage: String = "",
    val description: String,
    val images: List<String> = emptyList(),
    val date: String,
)

data class ComplaintsState(
    val complaints: List<Complaint> = emptyList(),
)

class ComplaintsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        ComplaintsState(
            complaints = listOf(
                Complaint(
                    id = "1",
                    userName = "أحمد محمد",
                    description = "المنتج وصل تالف والتغليف مفتوح، وعند التواصل مع خدمة العملاء لم أحصل على رد سريع",
                    images = listOf(
                        "https://picsum.photos/seed/c1a/400/300",
                        "https://picsum.photos/seed/c1b/400/300",
                    ),
                    date = "منذ ساعتين",
                ),
                Complaint(
                    id = "2",
                    userName = "سارة عبدالله",
                    description = "تأخر التوصيل أكثر من 3 أيام عن الموعد المحدد بدون أي إشعار أو تحديث",
                    images = listOf(
                        "https://picsum.photos/seed/c2a/400/300",
                    ),
                    date = "منذ يوم",
                ),
                Complaint(
                    id = "3",
                    userName = "خالد فهد",
                    description = "المنتج مختلف تماماً عن الصور المعروضة في التطبيق من حيث اللون والحجم",
                    images = listOf(
                        "https://picsum.photos/seed/c3a/400/300",
                        "https://picsum.photos/seed/c3b/400/300",
                        "https://picsum.photos/seed/c3c/400/300",
                    ),
                    date = "منذ 3 أيام",
                ),
                Complaint(
                    id = "4",
                    userName = "نورة سعيد",
                    description = "طلبت استرجاع المبلغ ولم يتم الرد حتى الآن رغم مرور أسبوع كامل",
                    images = emptyList(),
                    date = "منذ أسبوع",
                ),
            ),
        ),
    )
    val uiState: StateFlow<ComplaintsState> = _uiState.asStateFlow()
}
