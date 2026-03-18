package com.aait.domain.usecase.auth

import com.aait.domain.exceptions.ValidationException
import com.aait.domain.repository.HomeRepository
import com.aait.domain.util.CommonValidation.isValidEmail
import com.aait.domain.util.DataState
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class ForgetPasswordUseCase(private val homeRepository: HomeRepository) {
    operator fun invoke(email: String) = flow {
        if (!email.isValidEmail()) {
            emit(DataState.Error(ValidationException.InValidEmailException()))
        } else {
            emitAll(homeRepository.forgetPassword(email = email))
        }
    }
}