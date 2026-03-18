package com.aait.domain.exceptions

sealed class ValidationException : Exception() {
    data class InValidFirstNameException : ValidationException()

    data class InValidLastNameException : ValidationException()

    data class InValidPhoneException : ValidationException()

    data class InValidEmailException : ValidationException()

    data class InValidPasswordException : ValidationException()

    data class InValidConfirmPasswordException : ValidationException()

    data class InValidTermsException : ValidationException()

    data class InValidCodeException : ValidationException()
}
