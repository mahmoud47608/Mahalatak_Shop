package com.aait.domain.util

sealed class NetworkExceptions : Exception() {
    class UnknownException : NetworkExceptions()
    class ServerException : NetworkExceptions()
    class NotFoundException : NetworkExceptions()
    class TimeoutException : NetworkExceptions()
    class ConnectionException : NetworkExceptions()
    class AuthorizationException : NetworkExceptions()
    data class NeedActiveException(val msg: String) : NetworkExceptions()
    data class CustomException(val msg: String) : NetworkExceptions()
}
