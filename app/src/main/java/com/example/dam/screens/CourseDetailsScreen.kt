package com.example.dam.screens

import android.content.Context
import android.graphics.Paint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import com.example.dam.ui.theme.*

// Données statiques du parcours (Tunis - Exemple de route cycliste)
object TunisRouteData {
    val routePoints = listOf(
        GeoPoint(36.8065, 10.1815),  // Belvedere Park
        GeoPoint(36.8100, 10.1850),
        GeoPoint(36.8150, 10.1900),
        GeoPoint(36.8180, 10.1920),
        GeoPoint(36.8220, 10.1950),
        GeoPoint(36.8250, 10.1980),
        GeoPoint(36.8280, 10.2010),
        GeoPoint(36.8300, 10.2050),  // Lake Tunis
    )

    val departure = "Belvedere Park"
    val arrival = "Lake Tuins"
    val distance = "32.4 km"
    val duration = "1h 45min"
    val elevation = "150 m"
    val weather = "Sunny, 23°"
    val calories = "450 kcal"
    val speed = "27 km/h"
}

@Composable
fun CourseDetailsScreen(
    navController: NavHostController,
    showDropdown: Boolean = false
) {
    var showDetails by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        // Map en fond
        CourseMapView(
            modifier = Modifier.fillMaxSize(),
            routePoints = TunisRouteData.routePoints,
            context = context
        )

        // Top Bar
        TopBar(
            onBackClick = { navController.popBackStack() },
            onSettingsClick = { /* TODO */ }
        )

        // Map Controls (à droite)
        MapControlButtons(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp)
        )

        // Bottom Sheet avec détails
        AnimatedVisibility(
            visible = !showDetails,
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it })
        ) {
            CompactBottomSheet(
                onShowDetails = { showDetails = true }
            )
        }

        AnimatedVisibility(
            visible = showDetails,
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it })
        ) {
            ExpandedBottomSheet(
                onHideDetails = { showDetails = false }
            )
        }
    }
}

// ================= MAP VIEW AVEC ITINÉRAIRE =================

@Composable
fun CourseMapView(
    modifier: Modifier = Modifier,
    routePoints: List<GeoPoint>,
    context: Context
) {
    // Configuration OSMDroid
    Configuration.getInstance().load(
        context,
        context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE)
    )
    Configuration.getInstance().userAgentValue = context.packageName

    val mapView = remember { MapView(context) }

    AndroidView(
        modifier = modifier,
        factory = {
            mapView.apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)

                // Configuration de la carte
                minZoomLevel = 10.0
                maxZoomLevel = 19.0

                // Créer la Polyline (itinéraire en vert)
                val polyline = Polyline().apply {
                    setPoints(routePoints)
                    outlinePaint.color = android.graphics.Color.parseColor("#4ADE80")
                    outlinePaint.strokeWidth = 14f
                    outlinePaint.strokeCap = Paint.Cap.ROUND
                    outlinePaint.isAntiAlias = true
                }

                overlays.add(polyline)

                // Centrer sur le parcours
                val boundingBox = org.osmdroid.util.BoundingBox.fromGeoPoints(routePoints)
                post {
                    zoomToBoundingBox(boundingBox, true, 100)
                }
            }
        }
    )
}

// ================= TOP BAR =================

@Composable
fun TopBar(
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back button
        Surface(
            onClick = onBackClick,
            modifier = Modifier.size(44.dp),
            shape = CircleShape,
            color = Color.Black.copy(alpha = 0.6f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Text(
            text = "Course details",
            color = TextPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )

        // Settings button
//        Surface(
//            onClick = onSettingsClick,
//            modifier = Modifier.size(44.dp),
//            shape = CircleShape,
//            color = Color.Black.copy(alpha = 0.6f)
//        ) {

//            Box(contentAlignment = Alignment.Center) {
//                Icon(
//                    imageVector = Icons.Default.Settings,
//                    contentDescription = "Settings",
//                    tint = TextPrimary,
//                    modifier = Modifier.size(24.dp)
//                )
//            }
        }
    }


// ================= MAP CONTROLS =================

@Composable
fun MapControlButtons(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CourseMapControlButton(icon = "🗺️") // Layers
        CourseMapControlButton(icon = "➕") // Zoom in
        CourseMapControlButton(icon = "➖") // Zoom out
        Spacer(modifier = Modifier.height(100.dp))
        CourseMapControlButton(icon = "📍") // Location
    }
}

@Composable
fun CourseMapControlButton(icon: String) {
    Surface(
        modifier = Modifier.size(44.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color.White.copy(alpha = 0.95f)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(text = icon, fontSize = 20.sp)
        }
    }
}

// ================= COMPACT BOTTOM SHEET =================

@Composable
fun CompactBottomSheet(onShowDetails: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 100.dp),
        contentAlignment = Alignment.Center
    ) {
        // Bouton Show Details avec effet Glass
        Surface(
            onClick = onShowDetails,
            modifier = Modifier
                .width(160.dp)
                .height(48.dp),
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF1F1F1F).copy(alpha = 0.7f),
            shadowElevation = 8.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = Color(0xFF2A2A2A).copy(alpha = 0.4f),
                        shape = RoundedCornerShape(24.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "show details",
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

// ================= EXPANDED BOTTOM SHEET =================

@Composable
fun ExpandedBottomSheet(onHideDetails: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        color = Color(0xFF1A1A1A).copy(alpha = 0.26f),
        shadowElevation = 8.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFF2A2A2A).copy(alpha = 0.26f),
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(20.dp)
        ) {
            Column {
                // Barre de glissement
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(4.dp)
                        .align(Alignment.CenterHorizontally)
                        .background(TextSecondary.copy(alpha = 0.4f), RoundedCornerShape(2.dp))
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Départ et Arrivée
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    InfoCard(
                        icon = "📍",
                        label = "departure:",
                        value = TunisRouteData.departure,
                        modifier = Modifier.weight(1f)
                    )
                    InfoCard(
                        icon = "🏁",
                        label = "arrival:",
                        value = TunisRouteData.arrival,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Stats (2x2 grid)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        icon = "🌡️",
                        label = "Weather",
                        value = TunisRouteData.weather,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        icon = "⏱️",
                        label = "Duration",
                        value = TunisRouteData.duration,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        icon = "📏",
                        label = "Distance",
                        value = TunisRouteData.distance,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        icon = "⛰️",
                        label = "Elevation",
                        value = TunisRouteData.elevation,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Performance Chart
                PerformanceChart()

                Spacer(modifier = Modifier.height(12.dp))

                // Calories et Speed
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        icon = "🔥",
                        label = "Calories",
                        value = TunisRouteData.calories,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        icon = "⚡",
                        label = "Speed",
                        value = TunisRouteData.speed,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Settings button
                Surface(
                    onClick = { /* TODO */ },
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.End),
                    shape = RoundedCornerShape(12.dp),
                    color = GreenAccent
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = "⚙️", fontSize = 24.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Show Details button (pour cacher)
                Button(
                    onClick = onHideDetails,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(alpha = 0.15f),
                        contentColor = TextPrimary
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "show details",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

// ================= INFO CARD =================

@Composable
fun InfoCard(
    icon: String,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(70.dp),
        shape = RoundedCornerShape(12.dp),
        color = CardDark.copy(alpha = 0.6f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = icon, fontSize = 14.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = label,
                    color = TextSecondary,
                    fontSize = 11.sp
                )
            }
            Text(
                text = value,
                color = TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// ================= STAT CARD =================

@Composable
fun StatCard(
    icon: String,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(65.dp),
        shape = RoundedCornerShape(12.dp),
        color = CardDark.copy(alpha = 0.6f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = icon, fontSize = 14.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = label,
                    color = TextSecondary,
                    fontSize = 11.sp
                )
            }
            Text(
                text = value,
                color = TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// ================= PERFORMANCE CHART =================

@Composable
fun PerformanceChart() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        shape = RoundedCornerShape(16.dp),
        color = CardDark.copy(alpha = 0.6f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Performance: Average",
                color = TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color(0xFF2A2A2A), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "📈 Chart Placeholder",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }
        }
    }
}