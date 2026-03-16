package com.aait.domain.exceptions

sealed class ValidationException : Exception() {
    data object InValidFirstNameException : ValidationException() {
        private fun readResolve(): Any = InValidPhoneException
    }

    data object InValidLastNameException : ValidationException() {
        private fun readResolve(): Any = InValidPhoneException
    }

    data object InValidPhoneException : ValidationException() {
        private fun readResolve(): Any = InValidPhoneException
    }

    data object InValidEmailException : ValidationException() {
        private fun readResolve(): Any = InValidPhoneException
    }

    data object InValidPasswordException : ValidationException() {
        private fun readResolve(): Any = InValidPasswordException
    }

    data object InValidConfirmPasswordException : ValidationException() {
        private fun readResolve(): Any = InValidPasswordException
    }

    data object InValidTermsException : ValidationException() {
        private fun readResolve(): Any = InValidPasswordException
    }

    data object InValidCodeException : ValidationException() {
        private fun readResolve(): Any = InValidPasswordException
    }
}
