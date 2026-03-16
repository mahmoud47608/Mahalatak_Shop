package com.aait.ui.map.location

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.aait.domain.entity.general.LatLngModel
import com.google.android.gms.maps.model.LatLng

@Composable
fun LocationPermissionManager(
    location: LatLngModel? = null,
    onLocationPicked: (LatLngModel) -> Unit
) {
    val context = LocalContext.current
    var latLng by remember { mutableStateOf<LatLng?>(null) }
    var mapDescription by remember { mutableStateOf<String?>(null) }
    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    var isCurrentLocationRequired by remember { mutableStateOf(location == null) }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasLocationPermission = isGranted
        isCurrentLocationRequired = isGranted && isCurrentLocationRequired
    }


    LaunchedEffect(Unit) {
        if (!hasLocationPermission) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    LaunchedEffect(latLng) {
        latLng?.let {
            mapDescription = it.getAddress(context)
            onLocationPicked(LatLngModel(it.latitude, it.longitude, mapDescription))
        }
    }

    if (isCurrentLocationRequired && hasLocationPermission) {
        CurrentLocationHandler { currentLatLng, _ ->
            latLng = currentLatLng
            isCurrentLocationRequired = false
        }
    }
}
