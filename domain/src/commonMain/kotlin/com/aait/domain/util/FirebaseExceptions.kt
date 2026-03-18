package com.aait.domain.util

sealed class FirebaseExceptions : Exception() {
    class UnknownException : FirebaseExceptions()
    class NotFoundException : FirebaseExceptions()
    data class CustomException(val msg: String) : FirebaseExceptions()
}
