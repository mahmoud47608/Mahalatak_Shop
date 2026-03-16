package com.aait.domain.usecase.auth

import com.aait.domain.exceptions.ValidationException
import com.aait.domain.repository.HomeRepository
import com.aait.domain.util.CommonValidation.isValidPassword
import com.aait.domain.util.CommonValidation.isValidPhone
import com.aait.domain.util.DataState
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val homeRepository: HomeRepository) {
    operator fun invoke(
        countryCode: String,
        phone: String,
        password: String,
        deviceId: String,
        socialId: String?
    ) = flow {
        when {
            !phone.isValidPhone() -> emit(DataState.Error(ValidationException.InValidPhoneException()))
            !password.isValidPassword() -> emit(DataState.Error(ValidationException.InValidPasswordException()))
            else -> emitAll(
                homeRepository.login(
                    countryCode = countryCode,
                    phone = phone,
                    password = password,
                    deviceId = deviceId,
                    socialId = socialId
                )
            )
        }
    }
}