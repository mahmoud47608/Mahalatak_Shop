package com.aait.cycles.common.map.location

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Locale

suspend fun LatLng.getAddress(context: Context): String = withContext(Dispatchers.IO) {
    val geocoder = Geocoder(context, Locale.forLanguageTag(Locale.getDefault().language))
    try {
        @Suppress("DEPRECATION")
        val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
        if (!addresses.isNullOrEmpty()) {
            val address = addresses[0]
            listOfNotNull(
                address.locality,
                address.subAdminArea,
                address.adminArea
            ).joinToString(", ")
        } else ""
    } catch (_: IOException) {
        ""
    }
}
