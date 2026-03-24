package com.mahalatk.common.component.map

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.flow.distinctUntilChanged

@SuppressLint("MissingPermission")
@Composable
actual fun PlatformMapView(
    modifier: Modifier,
    initialLat: Double,
    initialLng: Double,
    onCameraMove: (lat: Double, lng: Double) -> Unit,
    isMyLocationEnabled: Boolean,
) {
    val cameraPositionState = rememberCameraPositionState()
    val isCameraInitialized = remember { mutableStateOf(false) }

    LaunchedEffect(initialLat, initialLng) {
        if (!isCameraInitialized.value) {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(
                LatLng(initialLat, initialLng),
                15f
            )
            isCameraInitialized.value = true
        }
    }

    LaunchedEffect(isCameraInitialized.value) {
        if (!isCameraInitialized.value) return@LaunchedEffect

        snapshotFlow { cameraPositionState.position.target }
            .distinctUntilChanged()
            .collect { latLng ->
                onCameraMove(latLng.latitude, latLng.longitude)
            }
    }

    val mapUiSettings = remember {
        MapUiSettings(
            zoomControlsEnabled = false,
            myLocationButtonEnabled = true,
        )
    }

    val mapProperties = remember(isMyLocationEnabled) {
        MapProperties(isMyLocationEnabled = isMyLocationEnabled)
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        uiSettings = mapUiSettings,
        properties = mapProperties,
    )
}
