package com.mahalatk.domain.usecase.auth

import com.mahalatk.domain.exceptions.ValidationException
import com.mahalatk.domain.repository.HomeRepository
import com.mahalatk.domain.util.CommonValidation.isValidPassword
import com.mahalatk.domain.util.CommonValidation.isValidPhone
import com.mahalatk.domain.util.DataState
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class LoginUseCase(private val homeRepository: HomeRepository) {
    operator fun invoke(
        countryCode: String,
        phone: String,
        password: String,
        deviceId: String,
        socialId: String?
    ) = flow {
        val errors = mutableListOf<ValidationException>()

        if (!phone.isValidPhone()) errors.add(ValidationException.InValidPhoneException())
        if (!password.isValidPassword()) errors.add(ValidationException.InValidPasswordException())

        if (errors.isNotEmpty()) {
            emit(DataState.Error(ValidationException.MultipleValidationException(errors)))
        } else {
            emitAll(
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