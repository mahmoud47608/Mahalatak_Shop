package com.aait.cycles.common.map.location

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.provider.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mahalatak.shared.generated.resources.*
import org.jetbrains.compose.resources.stringResource

//There is an issue when get location at first time after getting permission
@Composable
fun CurrentLocationHandler(
    onGetLocation: (LatLng?, String?) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val locationManager = remember {
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    var showLocationDialog by remember { mutableStateOf(false) }

    fun requestCurrentLocation() {
        val isLocationEnabled =
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (!isLocationEnabled) {
            showLocationDialog = true
        } else {
            showLocationDialog = false
            fetchLocationInfo(context, fusedLocationClient) { location, description ->
                onGetLocation(
                    location?.let { LatLng(it.latitude, it.longitude) },
                    description
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        requestCurrentLocation()
    }

    // Recheck when app resumes
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                requestCurrentLocation()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    if (showLocationDialog) {
        LocationServiceDisabledDialog(
            onOpenSettings = {
                context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            },
            onDismiss = { showLocationDialog = false }
        )
    }
}

@SuppressLint("MissingPermission")
private fun fetchLocationInfo(
    context: Context,
    fusedLocationClient: FusedLocationProviderClient,
    onAddressFetched: (Location?, String?) -> Unit
) {
    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
        if (location != null) {
            CoroutineScope(Dispatchers.Main).launch {
                val address = LatLng(location.latitude, location.longitude).getAddress(context)
                onAddressFetched(location, address)
            }
        } else {
            onAddressFetched(null, "Location not found")
        }
    }.addOnFailureListener {
        onAddressFetched(null, "Error fetching location: ${it.message}")
    }
}


@Composable
fun LocationServiceDisabledDialog(
    onOpenSettings: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(Res.string.enable_location)) },
        text = { Text(stringResource(Res.string.location_services_are_disabled_please_enable_location)) },
        confirmButton = {
            TextButton(onClick = onOpenSettings) { Text(stringResource(Res.string.settings)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(Res.string.cancel)) }
        }
    )
}
