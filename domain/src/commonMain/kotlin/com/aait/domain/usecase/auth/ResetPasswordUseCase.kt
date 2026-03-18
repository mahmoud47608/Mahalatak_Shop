package com.aait.domain.usecase.auth

import com.aait.domain.exceptions.ValidationException
import com.aait.domain.repository.HomeRepository
import com.aait.domain.util.CommonValidation.isValidPassword
import com.aait.domain.util.DataState
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class ResetPasswordUseCase(private val homeRepository: HomeRepository) {
    operator fun invoke(
        code: String,
        email: String,
        password: String,
        passwordConfirmation: String
    ) = flow {
        when {
            code.length != 4 -> emit(DataState.Error(ValidationException.InValidCodeException()))
            !password.isValidPassword() -> emit(DataState.Error(ValidationException.InValidPasswordException()))
            password != passwordConfirmation -> emit(DataState.Error(ValidationException.InValidConfirmPasswordException()))
            else -> emitAll(
                homeRepository.resetPassword(
                    code = code,
                    email = email,
                    password = password
                )
            )
        }
    }
}