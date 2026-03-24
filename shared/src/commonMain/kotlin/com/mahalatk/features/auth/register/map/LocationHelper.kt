package com.mahalatk.features.auth.register.map

import androidx.compose.runtime.Composable

/**
 * Request location permissions and get callback on result.
 */
@Composable
expect fun rememberLocationPermissionLauncher(
    onGranted: () -> Unit,
    onDenied: () -> Unit,
): () -> Unit

/**
 * Check if location permission is already granted.
 */
expect fun hasLocationPermission(): Boolean

/**
 * Get current device location as lat/lng pair.
 * Returns null if unavailable.
 */
expect suspend fun getCurrentDeviceLocation(): Pair<Double, Double>?

/**
 * Reverse geocode coordinates to a readable address string.
 */
expect suspend fun getAddressFromCoordinates(lat: Double, lng: Double): String
