package com.mahalatk.common.component.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.coroutines.resume

private lateinit var appContext: Context

@Composable
actual fun rememberLocationPermissionLauncher(
    onGranted: () -> Unit,
    onDenied: () -> Unit,
): () -> Unit {
    appContext = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.values.any { it }
        if (granted) onGranted() else onDenied()
    }

    return remember(launcher) {
        {
            launcher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                )
            )
        }
    }
}

actual fun hasLocationPermission(): Boolean {
    if (!::appContext.isInitialized) return false
    return ContextCompat.checkSelfPermission(
        appContext, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                appContext, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
}

@android.annotation.SuppressLint("MissingPermission")
actual suspend fun getCurrentDeviceLocation(): Pair<Double, Double>? {
    if (!::appContext.isInitialized) return null
    return suspendCancellableCoroutine { cont ->
        val client = LocationServices.getFusedLocationProviderClient(appContext)
        val cancellationToken = CancellationTokenSource()

        client.getCurrentLocation(
            Priority.PRIORITY_BALANCED_POWER_ACCURACY,
            cancellationToken.token
        )
            .addOnSuccessListener { location ->
                if (location != null) {
                    cont.resume(Pair(location.latitude, location.longitude))
                } else {
                    cont.resume(null)
                }
            }
            .addOnFailureListener {
                cont.resume(null)
            }

        cont.invokeOnCancellation { cancellationToken.cancel() }
    }
}

actual suspend fun getAddressFromCoordinates(lat: Double, lng: Double): String =
    withContext(Dispatchers.IO) {
        try {
            val geocoder = Geocoder(appContext, Locale.getDefault())
            val addresses = geocoder.getFromLocation(lat, lng, 1)
            addresses?.firstOrNull()?.getAddressLine(0) ?: "موقع محدد على الخريطة"
        } catch (_: Exception) {
            "موقع محدد على الخريطة"
        }
    }
