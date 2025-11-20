package com.aikeyboard.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aikeyboard.ui.settings.sections.*
import com.aikeyboard.ui.settings.viewmodel.SettingsViewModel
import android.content.Intent
import com.aikeyboard.ui.asrtesting.ASRTestingActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController(),
    onBack: () -> Unit = {}
) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: "main"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Keyboard Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Sidebar navigation
            NavigationRail(
                modifier = Modifier.fillMaxHeight()
            ) {
                NavigationRailItem(
                    selected = currentRoute == "main",
                    onClick = { navController.navigate("main") },
                    icon = { Icon(Icons.Default.Settings, "Settings") },
                    label = { Text("Main") }
                )
                NavigationRailItem(
                    selected = currentRoute == "appearance",
                    onClick = { navController.navigate("appearance") },
                    icon = { Icon(Icons.Default.Palette, "Appearance") },
                    label = { Text("Appearance") }
                )
                NavigationRailItem(
                    selected = currentRoute == "voice",
                    onClick = { navController.navigate("voice") },
                    icon = { Icon(Icons.Default.Mic, "Voice") },
                    label = { Text("Voice Input") }
                )
                NavigationRailItem(
                    selected = currentRoute == "benchmark",
                    onClick = { navController.navigate("benchmark") },
                    icon = { Icon(Icons.Default.Assessment, "Benchmark") },
                    label = { Text("Benchmark") }
                )
                NavigationRailItem(
                    selected = currentRoute == "upgrade",
                    onClick = { navController.navigate("upgrade") },
                    icon = { Icon(Icons.Default.Star, "Upgrade") },
                    label = { Text("Upgrade") }
                )
                NavigationRailItem(
                    selected = currentRoute == "about",
                    onClick = { navController.navigate("about") },
                    icon = { Icon(Icons.Default.Info, "About") },
                    label = { Text("About") }
                )
            }

            // Content
            NavHost(
                navController = navController,
                startDestination = "main"
            ) {
                composable("main") {
                    MainSettingsSection(navController = navController)
                }
                composable("appearance") {
                    AppearanceSettingsSection()
                }
                composable("voice") {
                    VoiceInputSettingsSection()
                }
                composable("benchmark") {
                    BenchmarkSection()
                }
                composable("upgrade") {
                    UpgradeToProSection()
                }
                composable("about") {
                    AboutSection(
                        onPrivacyPolicyClick = {
                            navController.navigate("privacy")
                        }
                    )
                }
                composable("privacy") {
                    PrivacyPolicySection()
                }
            }
        }
    }
}

