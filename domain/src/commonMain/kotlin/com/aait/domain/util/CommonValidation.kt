package com.aait.domain.util

object CommonValidation {

    fun String?.isValidPhone(): Boolean {
        return this?.isNotEmpty() == true && this.length >= 9
    }

    fun String?.isValidPassword(): Boolean {
        return this?.isNotEmpty() == true && this.length >= 8
    }
}
