package com.aait.domain.util

object CommonValidation {

    fun String?.isValidName(): Boolean {
        return this?.isNotEmpty() == true && this.length >= 3
    }

    fun String?.isValidPhone(): Boolean {
        return this?.isNotEmpty() == true && this.length >= 9
    }

    fun String?.isValidPassword(): Boolean {
        return this?.isNotEmpty() == true && this.length >= 8
    }

    fun String.isValidEmail(): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$".toRegex()
        return this.matches(emailRegex)
    }
}