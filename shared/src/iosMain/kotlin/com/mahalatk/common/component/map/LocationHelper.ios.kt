package com.mahalatk.common.component.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.mahalatk.features.auth.register.map.hasLocationPermission
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.CoreLocation.CLAuthorizationStatus
import platform.CoreLocation.CLGeocoder
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.CoreLocation.kCLLocationAccuracyBest
import platform.Foundation.NSError
import platform.darwin.NSObject
import kotlin.coroutines.resume

private val locationManager = CLLocationManager()

@Composable
actual fun rememberLocationPermissionLauncher(
    onGranted: () -> Unit,
    onDenied: () -> Unit,
): () -> Unit {
    val launcher = remember {
        {
            val delegate = object : NSObject(), CLLocationManagerDelegateProtocol {
                override fun locationManagerDidChangeAuthorization(manager: CLLocationManager) {
                    val status = manager.authorizationStatus
                    if (status == kCLAuthorizationStatusAuthorizedWhenInUse ||
                        status == kCLAuthorizationStatusAuthorizedAlways
                    ) {
                        onGranted()
                    } else if (status != CLAuthorizationStatus.MIN_VALUE) {
                        // Not determined yet — wait
                        onDenied()
                    }
                }
            }
            locationManager.delegate = delegate
            locationManager.requestWhenInUseAuthorization()
        }
    }
    return launcher
}

actual fun hasLocationPermission(): Boolean {
    val status = locationManager.authorizationStatus
    return status == kCLAuthorizationStatusAuthorizedWhenInUse ||
            status == kCLAuthorizationStatusAuthorizedAlways
}

@OptIn(ExperimentalForeignApi::class)
actual suspend fun getCurrentDeviceLocation(): Pair<Double, Double>? {
    if (!hasLocationPermission()) return null

    return suspendCancellableCoroutine { cont ->
        val delegate = object : NSObject(), CLLocationManagerDelegateProtocol {
            override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
                val location = didUpdateLocations.lastOrNull() as? CLLocation
                manager.stopUpdatingLocation()
                if (location != null) {
                    location.coordinate.useContents {
                        cont.resume(Pair(latitude, longitude))
                    }
                } else {
                    cont.resume(null)
                }
            }

            override fun locationManager(manager: CLLocationManager, didFailWithError: NSError) {
                manager.stopUpdatingLocation()
                cont.resume(null)
            }
        }

        val manager = CLLocationManager()
        manager.delegate = delegate
        manager.desiredAccuracy = kCLLocationAccuracyBest
        manager.startUpdatingLocation()

        cont.invokeOnCancellation {
            manager.stopUpdatingLocation()
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
actual suspend fun getAddressFromCoordinates(lat: Double, lng: Double): String {
    return suspendCancellableCoroutine { cont ->
        val geocoder = CLGeocoder()
        val location = CLLocation(latitude = lat, longitude = lng)

        geocoder.reverseGeocodeLocation(location) { placemarks, error ->
            if (error != null || placemarks.isNullOrEmpty()) {
                cont.resume("موقع محدد على الخريطة")
            } else {
                @Suppress("UNCHECKED_CAST")
                val placemark =
                    (placemarks as List<platform.CoreLocation.CLPlacemark>).firstOrNull()
                if (placemark != null) {
                    val parts = listOfNotNull(
                        placemark.thoroughfare,
                        placemark.subLocality,
                        placemark.locality,
                        placemark.administrativeArea,
                        placemark.country,
                    )
                    val address = if (parts.isNotEmpty()) parts.joinToString(", ")
                    else "موقع محدد على الخريطة"
                    cont.resume(address)
                } else {
                    cont.resume("موقع محدد على الخريطة")
                }
            }
        }
    }
}
