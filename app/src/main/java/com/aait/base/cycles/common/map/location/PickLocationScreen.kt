package com.aait.base.cycles.common.map.location

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.aait.base.common.component.utilis.noRippleClickable
import com.aait.domain.entity.general.LatLngModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import com.mahalatak.R
import kotlinx.coroutines.delay

@Composable
fun PickLocationScreen(
    modifier: Modifier = Modifier,
    title: String? = null,
    location: LatLngModel? = null,
    onBack: () -> Unit,
    onLocationPicked: (LatLngModel) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }
    var mapDescription by remember { mutableStateOf<String?>(null) }
    var showDeniedDialog by remember { mutableStateOf(false) }
    var isPermissionGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(
                        context, Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
        )
    }
    var shouldFetchCurrentLocation by remember { mutableStateOf(location == null) }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val isGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        isPermissionGranted = isGranted
        showDeniedDialog = !isGranted
        shouldFetchCurrentLocation = isGranted && shouldFetchCurrentLocation
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(location?.latitude ?: 0.0, location?.longitude ?: 0.0), 10f
        )
    }
    var hasAnimatedInitialPosition by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!isPermissionGranted) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                val isGranted = ContextCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(
                            context, Manifest.permission.ACCESS_COARSE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED

                if (isGranted && !isPermissionGranted) {
                    isPermissionGranted = true
                    showDeniedDialog = false
                    shouldFetchCurrentLocation = true
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(Unit) {
        MapsInitializer.initialize(context, MapsInitializer.Renderer.LATEST) {}
        location?.let {
            selectedLocation = LatLng(it.latitude ?: 0.0, it.longitude ?: 0.0)
        }
    }

    LaunchedEffect(selectedLocation) {
        if (selectedLocation != null && !hasAnimatedInitialPosition) {
            cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(selectedLocation!!, 15f))
            hasAnimatedInitialPosition = true
        }
    }

    LaunchedEffect(selectedLocation) {
        selectedLocation?.let {
            delay(500) // Debounce to avoid spamming
            mapDescription = it.getAddress(context)
        }
    }



    if (showDeniedDialog) {
        LocationServiceDisabledDialog(
            onOpenSettings = {
                context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            },
            onDismiss = { showDeniedDialog = false }
        )
    }

    if (shouldFetchCurrentLocation && isPermissionGranted) {
        CurrentLocationHandler { currentLatLng, _ ->
            selectedLocation = currentLatLng
            hasAnimatedInitialPosition = false
            shouldFetchCurrentLocation = false
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(White)
    ) {
        MapContent(
            cameraPositionState = cameraPositionState,
            isPermissionGranted = isPermissionGranted
        )

        Icon(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            imageVector = Icons.Filled.LocationOn,
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = null
        )

        HeaderSection(
            modifier = Modifier.align(Alignment.TopCenter),
            title = title,
            onBack = onBack
        )

        FooterSection(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            location = mapDescription
                ?: stringResource(R.string.unknown_location),
            onCurrentLocation = {
                if (isPermissionGranted) {
                    hasAnimatedInitialPosition = false
                    shouldFetchCurrentLocation = true
                } else {
                    showDeniedDialog = true
                }
            },
            onLocationPicked = {
                onLocationPicked(
                    LatLngModel(
                        latitude = selectedLocation?.latitude,
                        longitude = selectedLocation?.longitude,
                        mapDesc = mapDescription,
                    )
                )
            }
        )
    }

    LaunchedEffect(cameraPositionState.isMoving) {
        if (!cameraPositionState.isMoving) {
            selectedLocation = cameraPositionState.position.target
        }
    }
}

@Composable
private fun MapContent(
    cameraPositionState: CameraPositionState,
    isPermissionGranted: Boolean
) {
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(
            zoomControlsEnabled = false,
            myLocationButtonEnabled = false,
        ),
        properties = MapProperties(isMyLocationEnabled = isPermissionGranted)
    )
}

@Composable
private fun HeaderSection(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    title: String? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = White.copy(.9f))
            .padding(top = 32.dp, bottom = 24.dp)
            .background(color = Transparent),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.app_icon),
            contentDescription = null,
            modifier = Modifier.noRippleClickable { onBack() }
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = title ?: stringResource(R.string.pick_location),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun FooterSection(
    modifier: Modifier = Modifier,
    location: String,
    onCurrentLocation: () -> Unit,
    onLocationPicked: () -> Unit
) {
    Column(modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.MyLocation,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .noRippleClickable { onCurrentLocation() }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = White.copy(.9f),
                    shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                )
                .padding(16.dp)
        ) {
            Text(text = stringResource(R.string.location))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(
                    painter = painterResource(R.drawable.app_icon),
                    contentDescription = null
                )
                Text(text = location, maxLines = 2)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(White.copy(.1f)),
                onClick = onLocationPicked
            ) { Text(stringResource(R.string.confirm)) }
        }
    }
}
