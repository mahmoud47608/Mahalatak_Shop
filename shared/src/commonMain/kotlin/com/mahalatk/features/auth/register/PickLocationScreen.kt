package com.mahalatk.features.auth.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.mahalatk.common.component.button.DefaultButton
import com.mahalatk.features.auth.register.map.PlatformMapView
import com.mahalatk.features.auth.register.map.getAddressFromCoordinates
import com.mahalatk.features.auth.register.map.getCurrentDeviceLocation
import com.mahalatk.features.auth.register.map.hasLocationPermission
import com.mahalatk.features.auth.register.map.rememberLocationPermissionLauncher
import com.mahalatk.theme.MahalatkTheme
import kotlinx.coroutines.launch
import mahalatk.shared.generated.resources.Res
import mahalatk.shared.generated.resources.confirm
import mahalatk.shared.generated.resources.location_permission_required
import mahalatk.shared.generated.resources.pick_location
import org.jetbrains.compose.resources.stringResource

@Composable
fun PickLocationScreen(
    onBackWithResult: (lat: Double, lng: Double, address: String) -> Unit,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    var permissionGranted by remember { mutableStateOf(hasLocationPermission()) }
    var currentLat by remember { mutableStateOf(24.7136) }
    var currentLng by remember { mutableStateOf(46.6753) }
    var initialLat by remember { mutableStateOf(24.7136) }
    var initialLng by remember { mutableStateOf(46.6753) }
    var isInitialized by remember { mutableStateOf(false) }
    var currentAddress by remember { mutableStateOf("") }

    val requestPermission = rememberLocationPermissionLauncher(
        onGranted = {
            permissionGranted = true
            scope.launch {
                val loc = getCurrentDeviceLocation()
                if (loc != null && !isInitialized) {
                    initialLat = loc.first
                    initialLng = loc.second
                    currentLat = loc.first
                    currentLng = loc.second
                    isInitialized = true
                    currentAddress = getAddressFromCoordinates(loc.first, loc.second)
                }
            }
        },
        onDenied = { permissionGranted = false }
    )

    LaunchedEffect(Unit) {
        if (!permissionGranted) {
            requestPermission()
        } else {
            val loc = getCurrentDeviceLocation()
            if (loc != null && !isInitialized) {
                initialLat = loc.first
                initialLng = loc.second
                currentLat = loc.first
                currentLng = loc.second
                isInitialized = true
                currentAddress = getAddressFromCoordinates(loc.first, loc.second)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MahalatkTheme.white)
    ) {
        // Top bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp)
                .padding(horizontal = 8.dp)
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = MahalatkTheme.black
                )
            }
            Text(
                text = stringResource(Res.string.pick_location),
                style = MahalatkTheme.titleMedium,
                color = MahalatkTheme.black,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Map area
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center,
        ) {
            if (permissionGranted) {
                PlatformMapView(
                    modifier = Modifier.fillMaxSize(),
                    initialLat = initialLat,
                    initialLng = initialLng,
                    onCameraMove = { lat, lng ->
                        currentLat = lat
                        currentLng = lng
                        scope.launch {
                            currentAddress = getAddressFromCoordinates(lat, lng)
                        }
                    },
                    isMyLocationEnabled = true,
                )

                // Pin overlay (centered)
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = null,
                    tint = MahalatkTheme.primary,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(y = (-20).dp)
                        .size(48.dp)
                )
            } else {
                Text(
                    text = stringResource(Res.string.location_permission_required),
                    color = MahalatkTheme.hint,
                )
            }
        }

        // Confirm button
        if (permissionGranted) {
            DefaultButton(
                text = stringResource(Res.string.confirm),
                onClick = {
                    val address = currentAddress.ifBlank { "موقع محدد على الخريطة" }
                    onBackWithResult(currentLat, currentLng, address)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            )
        }
    }
}
