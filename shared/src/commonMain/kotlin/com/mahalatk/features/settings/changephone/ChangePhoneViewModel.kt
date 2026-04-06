package com.mahalatk.features.settings.changephone

import com.mahalatk.base.BaseViewModel
import com.mahalatk.base.managers.LoadingManager
import com.mahalatk.base.managers.MessageManager
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.please_enter_phone_number

class ChangePhoneViewModel(
    loadingManager: LoadingManager,
    messageManager: MessageManager,
) : BaseViewModel<ChangePhoneState, Nothing>(
    ChangePhoneState(),
    loadingManager,
    messageManager,
) {

    fun onPhoneChanged(phone: String) {
        updateState { copy(phone = phone, phoneError = null) }
    }

    fun validate(): Boolean {
        val state = uiState.value
        if (state.phone.isBlank() || state.phone.length < 9) {
            updateState { copy(phoneError = Res.string.please_enter_phone_number) }
            return false
        }
        return true
    }
}
