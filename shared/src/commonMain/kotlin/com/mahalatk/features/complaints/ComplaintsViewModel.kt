package com.mahalatk.features.complaints

import androidx.compose.runtime.Immutable
import com.mahalatk.base.SimpleViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class Complaint(
    val id: String,
    val userName: String,
    val userImage: String = "",
    val description: String,
    val images: ImmutableList<String> = persistentListOf(),
    val date: String,
)

@Immutable
data class ComplaintsState(
    val complaints: ImmutableList<Complaint> = persistentListOf(),
)

class ComplaintsViewModel : SimpleViewModel<ComplaintsState, Nothing>(
    ComplaintsState(
        complaints = persistentListOf(
                Complaint(
                    id = "1",
                    userName = "أحمد محمد",
                    description = "المنتج وصل تالف والتغليف مفتوح، وعند التواصل مع خدمة العملاء لم أحصل على رد سريع",
                    images = persistentListOf(
                        "https://picsum.photos/seed/c1a/400/300",
                        "https://picsum.photos/seed/c1b/400/300",
                    ),
                    date = "منذ ساعتين",
                ),
                Complaint(
                    id = "2",
                    userName = "سارة عبدالله",
                    description = "تأخر التوصيل أكثر من 3 أيام عن الموعد المحدد بدون أي إشعار أو تحديث",
                    images = persistentListOf(
                        "https://picsum.photos/seed/c2a/400/300",
                    ),
                    date = "منذ يوم",
                ),
                Complaint(
                    id = "3",
                    userName = "خالد فهد",
                    description = "المنتج مختلف تماماً عن الصور المعروضة في التطبيق من حيث اللون والحجم",
                    images = persistentListOf(
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
                    images = persistentListOf(),
                    date = "منذ أسبوع",
                ),
        ),
    ),
)
