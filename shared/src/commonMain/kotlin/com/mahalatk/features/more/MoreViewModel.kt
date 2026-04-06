package com.mahalatk.features.more

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.mahalatk.base.SimpleViewModel
import com.mahalatk.base.UserDataProvider
import com.mahalatk.domain.repository.PreferenceRepository
import com.mahalatk.domain.util.TokenCacheManager
import kotlinx.coroutines.launch

@Immutable
data class MoreState(
    val userName: String = "",
    val userImage: String = "",
    val isShopOwner: Boolean = true,
)

sealed interface MoreEvent {
    data object LoggedOut : MoreEvent
}

class MoreViewModel(
    private val userDataProvider: UserDataProvider,
    private val preferenceRepository: PreferenceRepository,
    private val tokenCacheManager: TokenCacheManager,
) : SimpleViewModel<MoreState, MoreEvent>(MoreState()) {

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            val authData = userDataProvider.getAuthData()
            val isShopOwner = authData?.userType == "shop_owner" || authData?.type == "shop_owner"
            val name = userDataProvider.getUserName()
            val image = userDataProvider.getUserImage()
            updateState {
                copy(
                    userName = name,
                    userImage = image,
                    isShopOwner = isShopOwner,
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            preferenceRepository.setUserData("")
            preferenceRepository.setToken("")
            tokenCacheManager.removeToken()
            preferenceRepository.setIsLogin(false)
            sendEvent(MoreEvent.LoggedOut)
        }
    }
}
