package com.mahalatk.domain.exceptions

sealed class ValidationException : Exception() {
    class InValidPhoneException : ValidationException()

    class InValidPasswordException : ValidationException()
}
