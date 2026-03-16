package com.aait.domain.usecase.auth

import com.aait.domain.exceptions.ValidationException
import com.aait.domain.repository.HomeRepository
import com.aait.domain.util.CommonValidation.isValidEmail
import com.aait.domain.util.CommonValidation.isValidName
import com.aait.domain.util.CommonValidation.isValidPassword
import com.aait.domain.util.CommonValidation.isValidPhone
import com.aait.domain.util.DataState
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RegisterUseCase @Inject constructor(private val homeRepository: HomeRepository) {
    operator fun invoke(
        firstName: String,
        lastName: String,
        countryCode: String,
        phone: String,
        email: String,
        password: String,
        confirmPassword: String,
        terms: Boolean
    ) = flow {
        when {
            !firstName.isValidName() -> emit(DataState.Error(ValidationException.InValidFirstNameException()))
            !lastName.isValidName() -> emit(DataState.Error(ValidationException.InValidLastNameException()))
            !phone.isValidPhone() -> emit(DataState.Error(ValidationException.InValidPhoneException()))
            !email.isValidEmail() -> emit(DataState.Error(ValidationException.InValidEmailException()))
            !password.isValidPassword() -> emit(DataState.Error(ValidationException.InValidPasswordException()))
            password != confirmPassword -> emit(DataState.Error(ValidationException.InValidConfirmPasswordException()))
            !terms -> emit(DataState.Error(ValidationException.InValidTermsException()))
            else -> emitAll(
                homeRepository.register(
                    firstName = firstName,
                    lastName = lastName,
                    countryCode = countryCode,
                    phone = phone,
                    email = email,
                    password = password
                )
            )
        }
    }
}