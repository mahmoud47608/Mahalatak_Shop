package com.mahalatk.domain.exceptions

sealed class ValidationException : Exception() {
    class InValidPhoneException : ValidationException()

    class InValidPasswordException : ValidationException()

    class MultipleValidationException(
        val errors: List<ValidationException>
    ) : ValidationException()
}
