package com.aait.domain.entity.general

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CountriesResponse(

    @SerialName("countries") val countries: List<DataItem>? = null
)