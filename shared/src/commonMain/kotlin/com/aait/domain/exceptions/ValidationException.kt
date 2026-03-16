package com.aait.domain.exceptions

sealed class ValidationException : Exception() {
    data class InValidFirstNameException : ValidationException() {
        private fun readResolve(): Any = InValidPhoneException()
    }

    data class InValidLastNameException : ValidationException() {
        private fun readResolve(): Any = InValidPhoneException()
    }

    data class InValidPhoneException : ValidationException() {
        private fun readResolve(): Any = InValidPhoneException()
    }

    data class InValidEmailException : ValidationException() {
        private fun readResolve(): Any = InValidPhoneException()
    }

    data class InValidPasswordException : ValidationException() {
        private fun readResolve(): Any = InValidPasswordException()
    }

    data class InValidConfirmPasswordException : ValidationException() {
        private fun readResolve(): Any = InValidPasswordException()
    }

    data class InValidTermsException : ValidationException() {
        private fun readResolve(): Any = InValidPasswordException()
    }

    data class InValidCodeException : ValidationException() {
        private fun readResolve(): Any = InValidPasswordException()
    }
}
