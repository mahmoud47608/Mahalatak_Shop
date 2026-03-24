package com.mahalatk.features.auth.register

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class LocationResult(
    val lat: Double,
    val lng: Double,
    val address: String,
)

/**
 * Simple shared holder for passing location pick result
 * from PickLocationScreen back to RegisterScreen.
 */
object LocationResultHolder {
    private val _result = MutableStateFlow<LocationResult?>(null)
    val result: StateFlow<LocationResult?> = _result.asStateFlow()

    fun setResult(lat: Double, lng: Double, address: String) {
        _result.value = LocationResult(lat, lng, address)
    }

    fun consume(): LocationResult? {
        val value = _result.value
        _result.value = null
        return value
    }
}
