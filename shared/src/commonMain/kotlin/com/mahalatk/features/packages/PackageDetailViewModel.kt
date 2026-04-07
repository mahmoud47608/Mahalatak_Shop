package com.mahalatk.features.packages

import androidx.lifecycle.viewModelScope
import com.mahalatk.base.SimpleViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PackageDetailViewModel : SimpleViewModel<PackageDetailState, Nothing>(PackageDetailState()) {

    fun loadPackage(item: PackageItem) {
        updateState { copy(packageItem = item) }
    }

    fun subscribe() {
        val pkg = uiState.value.packageItem ?: return
        if (pkg.status != PackageStatus.AVAILABLE) return

        updateState { copy(isSubscribing = true) }

        viewModelScope.launch {
            // Mock API call until backend is ready
            delay(1000)
            updateState {
                copy(
                    isSubscribing = false,
                    showSubscribeSuccess = true,
                    packageItem = packageItem?.copy(status = PackageStatus.PENDING),
                )
            }
        }
    }

    fun dismissSuccess() {
        updateState { copy(showSubscribeSuccess = false) }
    }
}
