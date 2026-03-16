package com.aait.domain.exceptions

sealed class ValidationException : Exception() {
    class InValidFirstNameException : ValidationException()
    class InValidLastNameException : ValidationException()
    class InValidPhoneException : ValidationException()
    class InValidEmailException : ValidationException()
    class InValidPasswordException : ValidationException()
    class InValidConfirmPasswordException : ValidationException()
    class InValidTermsException : ValidationException()
    class InValidCodeException : ValidationException()
}
