package com.aait.domain.util

sealed class NetworkExceptions : Exception() {
    data class UnknownException : NetworkExceptions()
    data class ServerException : NetworkExceptions()
    data class NotFoundException : NetworkExceptions()
    data class TimeoutException : NetworkExceptions()
    data class ConnectionException : NetworkExceptions()
    data class AuthorizationException : NetworkExceptions()
    data class NeedActiveException(val msg: String) : NetworkExceptions()
    data class CustomException(val msg: String) : NetworkExceptions()
}