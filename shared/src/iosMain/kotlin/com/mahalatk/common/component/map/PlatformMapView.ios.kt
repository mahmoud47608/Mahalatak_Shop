package com.mahalatk.common.component.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.CoreLocation.CLLocationCoordinate2DMake
import platform.MapKit.MKCoordinateRegionMakeWithDistance
import platform.MapKit.MKMapView
import platform.MapKit.MKMapViewDelegateProtocol
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun PlatformMapView(
    modifier: Modifier,
    initialLat: Double,
    initialLng: Double,
    onCameraMove: (lat: Double, lng: Double) -> Unit,
    isMyLocationEnabled: Boolean,
) {
    val delegate = remember {
        object : NSObject(), MKMapViewDelegateProtocol {
            override fun mapView(mapView: MKMapView, regionDidChangeAnimated: Boolean) {
                mapView.centerCoordinate.useContents {
                    onCameraMove(latitude, longitude)
                }
            }
        }
    }

    UIKitView(
        modifier = modifier,
        factory = {
            MKMapView().apply {
                this.delegate = delegate
                showsUserLocation = isMyLocationEnabled
                val center = CLLocationCoordinate2DMake(initialLat, initialLng)
                val region = MKCoordinateRegionMakeWithDistance(center, 2000.0, 2000.0)
                setRegion(region, animated = false)
            }
        },
        update = { mapView ->
            mapView.showsUserLocation = isMyLocationEnabled
        }
    )

    DisposableEffect(Unit) {
        onDispose { }
    }
}
