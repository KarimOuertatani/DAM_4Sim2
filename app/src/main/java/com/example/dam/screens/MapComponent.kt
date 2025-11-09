package com.example.dam.screens

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun OpenStreetMapView(
    modifier: Modifier = Modifier,
    centerLatitude: Double = 36.8065,  // Centre de la Tunisie (Tunis)
    centerLongitude: Double = 10.1815,
    zoomLevel: Double = 8.0  // Zoom pour voir toute la Tunisie
) {
    val context = LocalContext.current

    // Configuration d'OSMDroid
    Configuration.getInstance().load(
        context,
        context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE)
    )
    Configuration.getInstance().userAgentValue = context.packageName

    val mapView = rememberMapViewWithLifecycle()

    AndroidView(
        modifier = modifier,
        factory = {
            mapView.apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)

                // Centrer sur la Tunisie
                controller.setZoom(zoomLevel)
                controller.setCenter(GeoPoint(centerLatitude, centerLongitude))

                // Limiter la carte à la Tunisie
                // Nord: 37.5°, Sud: 30.2°, Est: 11.6°, Ouest: 7.5°
                setScrollableAreaLimitDouble(
                    org.osmdroid.util.BoundingBox(
                        37.5,  // Nord
                        11.6,  // Est
                        30.2,  // Sud
                        7.5    // Ouest
                    )
                )

                // Limites de zoom
                minZoomLevel = 6.0  // Zoom minimum (voir toute la Tunisie)
                maxZoomLevel = 19.0 // Zoom maximum (niveau rue)
            }
        }
    )
}

@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    return remember {
        MapView(context)
    }
}

