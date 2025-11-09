package com.example.dam.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavHostController

data class Participant(
    val name: String,
    val age: Int,
    val level: String,
    val email: String,
    val imageUrl: String? = null
)

@Composable
fun ChallengeScreen(
    onBackClick: () -> Unit = {},
    navController: NavHostController,
    showDropdown: Boolean
) {
    var selectedDifficulty by remember { mutableStateOf(3) }

    val participants = remember {
        listOf(
            Participant("Yassine Ben Ali", 25, "Intermediate", "email@email.domain.mail"),
            Participant("Amira Gharbi", 22, "Beginner", "email@email.domain.mail"),
            Participant("Karim Trabelsi", 28, "Advance", "email@email.domain.mail"),
            Participant("Sami Jaziri", 30, "Expert", "email@email.domain.mail")
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 22.dp, vertical = 22.dp)
        ) {
            // --- Glassy back button + Title ---
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = Modifier.padding(bottom = 20.dp)
////            ) {
//                Box(
//                    modifier = Modifier
//                        .size(42.dp)
//                        .background(
//                            color = Color.White.copy(alpha = 0.15f),
//                            shape = CircleShape
//                        )
//                        .border(
//                            width = 1.5.dp,
//                            color = Color.White.copy(alpha = 0.30f),
//                            shape = CircleShape
//                        )
//                        .clickable { navController.popBackStack() },
//                    contentAlignment = Alignment.Center
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.ArrowBack,
//                        contentDescription = "Back",
//                        tint = Color.White,
//                        modifier = Modifier.size(24.dp)
//                    )
//                }

                Spacer(modifier = Modifier.width(12.dp))

//                Text(
//                    text = "Challenge",
//                    color = Color.White,
//                    fontSize = 28.sp,
//                    fontWeight = FontWeight.Bold
//                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Date Selectors
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        DateSelector(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Default.CalendarToday,
                            date = "15/10/2025"
                        )
                        DateSelector(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Default.AccessTime,
                            date = "14:30"
                        )
                    }
                }

                // Map with Route
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        // Map Component
                        MapWithRoute(
                            modifier = Modifier.fillMaxSize()
                        )

                        // Eye Icon (top left)
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = "View",
                            modifier = Modifier
                                .padding(12.dp)
                                .size(28.dp)
                                .align(Alignment.TopStart)
                        )

                        // Stats Panel (right side)
                        Column(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(8.dp)
                                .width(120.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            StatItem(
                                icon = "🌤️",
                                label = "weather",
                                value = "187",
                                unit = "°C"
                            )
                            StatItem(
                                icon = "⏱️",
                                label = "Time",
                                value = "1h 45min",
                                unit = ""
                            )
                            StatItem(
                                icon = "📍",
                                label = "Distance",
                                value = "36.1",
                                unit = "km"
                            )

                            Button(
                                onClick = { },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(36.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    "Equipment",
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }

                // Difficulty Selector
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Difficulty",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            (1..5).forEach { level ->
                                DifficultyButton(
                                    level = level,
                                    isSelected = selectedDifficulty == level,
                                    onClick = { selectedDifficulty = level },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }

                // Participants Section
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Number of participants: ${participants.size}",
                            fontSize = 14.sp
                        )
                        TextButton(onClick = { }) {
                            Text(
                                "View all",
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                // Participants List
                items(participants) { participant ->
                    ParticipantItem(participant)
                }

                // Participate Button
                item {
                    Button(
                        onClick = { },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "Participate",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }


@Composable
fun DateSelector(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    date: String
) {
    Row(
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )
        Text(
            date,
            fontSize = 14.sp
        )
    }
}

@Composable
fun MapWithRoute(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        OpenStreetMapView(
            modifier = Modifier.fillMaxSize(),
            centerLatitude = 36.8065,
            centerLongitude = 10.1815,
            zoomLevel = 13.0
        )
    }
}

@Composable
fun StatItem(
    icon: String,
    label: String,
    value: String,
    unit: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
            .padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                icon,
                fontSize = 12.sp
            )
            Text(
                label,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                fontSize = 10.sp
            )
        }
        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.padding(top = 2.dp)
        ) {
            Text(
                value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            if (unit.isNotEmpty()) {
                Text(
                    unit,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    fontSize = 10.sp,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }
        }
    }
}

@Composable
fun DifficultyButton(
    level: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            level.toString(),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun ParticipantItem(participant: Participant) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                participant.name.first().toString(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Info
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                "${participant.name} - ${participant.age} yo - ${participant.level}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                participant.email,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}