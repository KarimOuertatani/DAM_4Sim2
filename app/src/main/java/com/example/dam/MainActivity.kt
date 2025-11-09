package com.example.dam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.dam.screens.*
import com.example.dam.ui.theme.DAMTheme
import com.example.dam.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DAMTheme {
                val navController = rememberNavController()
                MainAppScaffold(navController)
            }
        }
    }
}

// ================= SCAFFOLD PRINCIPAL =================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScaffold(navController: NavHostController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    // Routes avec BottomNav
    val routesWithBottomNav = listOf(
        "main",
        "profile",
        "editProfile1",
        "editProfile2",
        "newchallenge"
    )

    // Routes avec TopBar (titre + dropdown)
    val routesWithTopBar = listOf(
        "profile",
        "editProfile1",
        "editProfile2",
        "newchallenge",
        "map",

        "challenge"
    )

    // Routes avec bouton Back
    val routesWithBackButton = listOf(
        "challenge",
        "editProfile1",
        "editProfile2"
    )

    val showBottomNav = currentRoute in routesWithBottomNav
    val showTopBar = currentRoute in routesWithTopBar
    val showBackButton = currentRoute in routesWithBackButton

    var dropdownExpanded by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark),
        containerColor = BackgroundDark,
        topBar = {
            if (showTopBar) {
                Surface(
                    color = BackgroundDark,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 22.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Left side: Back button + Title
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            // Glassy Back Button
                            if (showBackButton) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .background(
                                            color = Color.White.copy(alpha = 0.15f),
                                            shape = CircleShape
                                        )
                                        .border(
                                            width = 1.5.dp,
                                            color = Color.White.copy(alpha = 0.30f),
                                            shape = CircleShape
                                        )
                                        .shadow(
                                            elevation = 8.dp,
                                            shape = CircleShape,
                                            ambientColor = Color.Black.copy(alpha = 0.1f),
                                            spotColor = Color.Black.copy(alpha = 0.15f)
                                        )
                                        .clickable { navController.popBackStack() },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = "Back",
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                            }

                            // Title
                            Text(
                                text = when (currentRoute) {
                                    "profile" -> "Profile"
                                    "editProfile1" -> "Edit Profile"
                                    "editProfile2" -> "Edit Profile"
                                    "challenge" -> "Challenge"
                                    "newchallenge" -> "New Challenge"
                                    "map" -> "Map"


                                    else -> ""
                                },
                                color = TextPrimary,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Right side: Dropdown Menu
                        Box {
                            DropdownChevron(onClick = { dropdownExpanded = true })
                            ProfileDropdownMenu(
                                expanded = dropdownExpanded,
                                onDismiss = { dropdownExpanded = false },
                                navController = navController
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            if (showBottomNav) {
                BottomNavigationBar(
                    currentRoute = currentRoute ?: "main",
                    navController = navController
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AppNavigation(navController)
        }
    }
}

// ================= NAVIGATION =================

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        composable("main") {
            MainScreen()
        }
        composable("editProfile1") {
            EditProfile1Screen(
                navController = navController,
                showDropdown = true
            )
        }
        composable("editProfile2") {
            EditProfile2Screen(
                navController = navController,
                showDropdown = true
            )
        }
        composable("profile") {
            ProfileScreen(
                navController = navController,
                showDropdown = true
            )
        }
        composable("newchallenge") {
            NewChallengeScreen(
                navController = navController,
                showDropdown = true
            )
        }
        composable("map") {
            MapScreen(
                navController = navController,
                showDropdown = false
            )
        }
        composable("challenge") {
            ChallengeScreen(
                navController = navController,
                showDropdown = false
            )
        }

        composable("course") {
            CourseDetailsScreen(
                navController = navController,
                showDropdown = false
            )
        }
    }
}

// ================= MAIN SCREEN =================

@Composable
fun MainScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = "🏠", fontSize = 64.sp)
            Text(text = "Home Screen", color = TextPrimary, fontSize = 24.sp)
            Text(
                text = "Tab sur les icônes en bas",
                color = TextSecondary,
                fontSize = 14.sp
            )
        }
    }
}

// ================= DROPDOWN MENU =================

@Composable
fun ProfileDropdownMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        modifier = modifier
            .background(Color(0xFF1F1F1F))
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        // Profile
        DropdownMenuItem(
            text = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = TextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "Profile", color = TextPrimary, fontSize = 15.sp)
                }
            },
            onClick = {
                navController.navigate("profile")
                onDismiss()
            }
        )

        // Saved
        DropdownMenuItem(
            text = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Bookmark,
                        contentDescription = "Saved",
                        tint = TextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "Saved", color = TextPrimary, fontSize = 15.sp)
                }
            },
            onClick = { onDismiss() }
        )

        // Help Center
        DropdownMenuItem(
            text = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Help,
                        contentDescription = "Help Center",
                        tint = TextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "Help Center", color = TextPrimary, fontSize = 15.sp)
                }
            },
            onClick = { onDismiss() }
        )

        // Settings
        DropdownMenuItem(
            text = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = TextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "Settings", color = TextPrimary, fontSize = 15.sp)
                }
            },
            onClick = { onDismiss() }
        )

        HorizontalDivider(color = Color(0xFF2F2F2F), thickness = 1.dp)

        // Logout
        DropdownMenuItem(
            text = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "Logout",
                        tint = Color(0xFFEF4444),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Logout",
                        color = Color(0xFFEF4444),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            },
            onClick = { onDismiss() }
        )
    }
}

// ================= CHEVRON =================

@Composable
fun DropdownChevron(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(48.dp)
    ) {
        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = "Menu",
            tint = TextSecondary,
            modifier = Modifier.size(32.dp)
        )
    }
}

// ================= BOTTOM NAV =================

@Composable
fun BottomNavigationBar(
    currentRoute: String,
    navController: NavHostController
) {
    val routeToTab = mapOf(
        "main" to 0,
        "profile" to 3,
        "editProfile1" to 3,
        "editProfile2" to 3,
        "newchallenge" to 2
    )

    val selectedTab = routeToTab[currentRoute] ?: 0

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .padding(horizontal = 20.dp, vertical = 10.dp),
        shape = RoundedCornerShape(30.dp),
        color = Color(0xFF1F1F1F).copy(alpha = 0.8f),
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color(0xFF1F1F1F).copy(alpha = 0.6f),
                    shape = RoundedCornerShape(30.dp)
                )
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem("🏠", "Home", selectedTab == 0) {
                navController.navigate("main")
            }
            BottomNavItem("🗺", "Map", selectedTab == 1) {
                navController.navigate("map")
            }
            BottomNavItem("➕", "Community", selectedTab == 2) {
                navController.navigate("newchallenge")
            }
            BottomNavItem("👤", "Profile", selectedTab == 3) {
                navController.navigate("profile")
            }
        }
    }
}

@Composable
fun BottomNavItem(
    icon: String,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.size(60.dp),
        shape = CircleShape,
        color = if (isSelected) GreenAccent.copy(alpha = 0.2f) else Color.Transparent
    ) {
        Box(contentAlignment = Alignment.Center) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = icon,
                    fontSize = 24.sp,
                    color = if (isSelected) GreenAccent else TextSecondary
                )
                if (isSelected) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Box(
                        modifier = Modifier
                            .size(4.dp)
                            .background(GreenAccent, CircleShape)
                    )
                }
            }
        }
    }
}