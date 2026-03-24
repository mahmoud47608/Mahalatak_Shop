package com.mahalatk.features.auth.register.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Platform-specific map view.
 * Android: Google Maps Compose
 * iOS: MapKit MKMapView via UIKitView
 */
@Composable
expect fun PlatformMapView(
    modifier: Modifier = Modifier,
    initialLat: Double,
    initialLng: Double,
    onCameraMove: (lat: Double, lng: Double) -> Unit,
    isMyLocationEnabled: Boolean,
)
