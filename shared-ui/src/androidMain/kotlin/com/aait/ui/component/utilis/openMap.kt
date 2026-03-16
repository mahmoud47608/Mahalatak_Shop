package com.aait.ui.component.utilis

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

fun Context.openMap(lat: Double, lng: Double, description: String) {
    val gmmIntentUri = "geo:$lat,$lng?q=$lat,$lng($description)".toUri()
    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
    mapIntent.setPackage("com.google.android.apps.maps")
    if (mapIntent.resolveActivity(this.packageManager) != null) {
        this.startActivity(mapIntent)
    }
}
