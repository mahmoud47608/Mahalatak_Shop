package com.mahalatk.features.packages

import androidx.lifecycle.viewModelScope
import com.mahalatk.base.SimpleViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PackagesViewModel : SimpleViewModel<PackagesState, Nothing>(PackagesState()) {

    init {
        loadPackages()
    }

    private fun loadPackages() {
        viewModelScope.launch {
            // Mock data until backend is ready
            delay(800)
            val mockPackages = persistentListOf(
                PackageItem(
                    1,
                    "رسائل جماعية",
                    "أرسل رسائل لجميع عملائك",
                    "150",
                    "messages",
                    "",
                    PackageStatus.SUBSCRIBED
                ),
                PackageItem(
                    2,
                    "سلايدر الهوم",
                    "أضف بانر إعلاني في الصفحة الرئيسية",
                    "200",
                    "home_slider",
                    "",
                    PackageStatus.SUBSCRIBED
                ),
                PackageItem(
                    3,
                    "سلايدر المور",
                    "أضف بانر في صفحة المزيد",
                    "120",
                    "more_slider",
                    "",
                    PackageStatus.SUBSCRIBED
                ),
                PackageItem(
                    4,
                    "سلايدر السلة",
                    "أضف بانر في صفحة السلة",
                    "100",
                    "cart_slider",
                    "",
                    PackageStatus.PENDING
                ),
                PackageItem(
                    5,
                    "الأكثر رواجاً",
                    "ظهور محلك في قسم المحلات الأكثر رواجاً",
                    "300",
                    "most_popular",
                    "",
                    PackageStatus.AVAILABLE
                ),
            )
            updateState { copy(packages = mockPackages, isLoading = false) }
        }
    }

    fun onSubscribeSuccess(packageId: Int) {
        updateState {
            copy(
                packages = packages.map {
                    if (it.id == packageId) it.copy(status = PackageStatus.PENDING) else it
                }.toImmutableList()
            )
        }
    }
}
