package com.mahalatk.domain.entity.general

import kotlinx.serialization.Serializable

@Serializable
data class LatLngModel(
    val latitude: Double?,
    val longitude: Double?,
    val mapDesc: String?
)