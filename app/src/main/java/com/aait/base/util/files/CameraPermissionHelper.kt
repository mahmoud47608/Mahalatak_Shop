package com.aait.base.util.files

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

/**
 * Check if camera permission is granted
 */
fun Context.hasCameraPermission(): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED
}

/**
 * Composable function to request camera permission
 * Returns a lambda that when invoked will request the permission
 *
 * Usage:
 * ```
 * val requestPermission = rememberCameraPermissionRequest(
 *     onPermissionGranted = { /* Handle granted */ },
 *     onPermissionDenied = { /* Handle denied */ }
 * )
 *
 * Button(onClick = { requestPermission() }) {
 *     Text("Enable Camera")
 * }
 * ```
 */
@Composable
fun rememberCameraPermissionRequest(
    onPermissionGranted: () -> Unit = {},
    onPermissionDenied: () -> Unit = {}
): () -> Unit {
    val context = LocalContext.current

    // Use rememberUpdatedState to ensure callbacks are always current
    val currentOnPermissionGranted by rememberUpdatedState(onPermissionGranted)
    val currentOnPermissionDenied by rememberUpdatedState(onPermissionDenied)

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            currentOnPermissionGranted()
        } else {
            currentOnPermissionDenied()
        }
    }

    return remember {
        {
            if (context.hasCameraPermission()) {
                currentOnPermissionGranted()
            } else {
                launcher.launch(Manifest.permission.CAMERA)
            }
        }
    }
}
