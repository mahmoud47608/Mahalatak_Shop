package com.mahalatk.domain.exceptions

sealed class ValidationException : Exception() {
    class InValidPhoneException : ValidationException()
    class InValidPasswordException : ValidationException()

    // Add Product validations
    data object EmptyProductNameArException : ValidationException()
    data object EmptyProductNameEnException : ValidationException()
    data object EmptyDescriptionArException : ValidationException()
    data object EmptyDescriptionEnException : ValidationException()
    data object EmptyImagesException : ValidationException()
    data object EmptyCategoryException : ValidationException()
    data object EmptyPriceException : ValidationException()
    data object InvalidPriceException : ValidationException()
    data object EmptyDiscountTypeException : ValidationException()
    data object EmptyDiscountValueException : ValidationException()
    data object InvalidDiscountValueException : ValidationException()

    class MultipleValidationException(
        val errors: List<ValidationException>
    ) : ValidationException()
}
