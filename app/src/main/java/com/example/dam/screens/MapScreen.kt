package com.example.dam.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.graphics.Color
import com.example.dam.ui.theme.BackgroundDark
import com.example.dam.ui.theme.TextPrimary

@Composable
fun MapScreen(navController: NavHostController, showDropdown: Boolean) {
    val context = LocalContext.current
    val mapView = rememberMapViewWithLifecycle()

    // Liste statique des points
    val ridePoints = listOf(
        Triple(36.81897, 10.16579, "24/01/2025"),
        Triple(36.8065, 10.1815, "25/01/2025"),
        Triple(36.8021, 10.1300, "24/11/2025"),
        Triple(36.7525, 10.2800, "14/01/2025"),
        Triple(36.8300, 10.2000, "16/07/2025"),
        Triple(36.7900, 10.2300, "21/02/2025")
    )

    // Configuration initiale de la carte
    LaunchedEffect(Unit) {
        setupMap(context, mapView)
        setupStaticMarkers(context, mapView, ridePoints, navController)

        // Centrer la carte sur Tunis avec un zoom raisonnable
        val center = GeoPoint(36.81897, 10.16579)
        mapView.controller.setZoom(11.0)
        mapView.controller.setCenter(center)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        // --- Header ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1E1E1E))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Titre au centre
            Text(
                text = "Map",
                color = TextPrimary,
                fontSize = 20.sp,
                modifier = Modifier.weight(1f),
            )

            // Bouton X à droite
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.White
                )
            }
        }

        // --- Carte ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // occupe tout l'espace disponible
                .padding(bottom = 60.dp) // espace réservé pour la BottomNavbar
        ) {
            AndroidView(
                factory = { mapView },
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}

// Initialisation de la carte
private fun setupMap(context: Context, mapView: MapView) {
    Configuration.getInstance().load(context, context.getSharedPreferences("osm_prefs", 0))
    mapView.setTileSource(TileSourceFactory.MAPNIK)
    mapView.setMultiTouchControls(true)
}

// Ajout des marqueurs avec navigation
private fun setupStaticMarkers(
    context: Context,
    mapView: MapView,
    ridePoints: List<Triple<Double, Double, String>>,
    navController: NavHostController
) {
    mapView.overlays.clear()

    for ((lat, lon, date) in ridePoints) {
        val marker = Marker(mapView)
        marker.position = GeoPoint(lat, lon)
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.title = date

        // Ajouter un listener pour le clic sur le marqueur
        marker.setOnMarkerClickListener { clickedMarker, _ ->
            // Navigation vers ChallengeScreen
            navController.navigate("challenge")
            true // Retourne true pour indiquer que l'événement est géré
        }

        mapView.overlays.add(marker)
    }

    mapView.invalidate()
}