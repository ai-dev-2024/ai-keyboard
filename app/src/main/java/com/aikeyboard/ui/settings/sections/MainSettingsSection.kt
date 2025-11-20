package com.aikeyboard.ui.settings.sections

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun MainSettingsSection(
    navController: NavController? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "General Settings",
            style = MaterialTheme.typography.headlineSmall
        )

        Divider()
        
        // Premium Upgrade Banner
        if (navController != null) {
            PremiumBanner(
                onUpgradeClick = { navController.navigate("upgrade") }
            )
            Divider()
        }

        SwitchPreference(
            title = "Auto-correct",
            checked = true,
            onCheckedChange = { }
        )

        SwitchPreference(
            title = "Gesture Typing",
            checked = true,
            onCheckedChange = { }
        )

        SwitchPreference(
            title = "Sound on Keypress",
            checked = false,
            onCheckedChange = { }
        )

        SwitchPreference(
            title = "Haptic Feedback",
            checked = true,
            onCheckedChange = { }
        )
    }
}

@Composable
fun SwitchPreference(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

