package com.aikeyboard.ui.settings.sections

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aikeyboard.shared.util.KeyboardUtils
import kotlinx.coroutines.delay

@Composable
fun MainSettingsSection(
    navController: NavController? = null
) {
    val context = LocalContext.current
    var isKeyboardEnabled by remember { mutableStateOf(false) }
    var isKeyboardDefault by remember { mutableStateOf(false) }
    var checkStatus by remember { mutableStateOf(true) }
    
    // Check keyboard status periodically
    LaunchedEffect(checkStatus) {
        while (true) {
            isKeyboardEnabled = KeyboardUtils.isKeyboardEnabled(context)
            isKeyboardDefault = KeyboardUtils.isKeyboardDefault(context)
            delay(1000) // Check every second
        }
    }
    
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

        // Keyboard Status Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = when {
                    isKeyboardDefault -> MaterialTheme.colorScheme.primaryContainer
                    isKeyboardEnabled -> MaterialTheme.colorScheme.secondaryContainer
                    else -> MaterialTheme.colorScheme.errorContainer
                }
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = when {
                            isKeyboardDefault -> Icons.Default.CheckCircle
                            isKeyboardEnabled -> Icons.Default.Info
                            else -> Icons.Default.Warning
                        },
                        contentDescription = null,
                        tint = when {
                            isKeyboardDefault -> MaterialTheme.colorScheme.onPrimaryContainer
                            isKeyboardEnabled -> MaterialTheme.colorScheme.onSecondaryContainer
                            else -> MaterialTheme.colorScheme.onErrorContainer
                        }
                    )
                    Text(
                        text = when {
                            isKeyboardDefault -> "AI Keyboard is your default keyboard ✓"
                            isKeyboardEnabled -> "AI Keyboard is enabled"
                            else -> "AI Keyboard is not enabled"
                        },
                        style = MaterialTheme.typography.titleMedium,
                        color = when {
                            isKeyboardDefault -> MaterialTheme.colorScheme.onPrimaryContainer
                            isKeyboardEnabled -> MaterialTheme.colorScheme.onSecondaryContainer
                            else -> MaterialTheme.colorScheme.onErrorContainer
                        }
                    )
                }
                
                if (!isKeyboardEnabled) {
                    Text(
                        text = "Enable AI Keyboard in system settings to use it",
                        style = MaterialTheme.typography.bodySmall
                    )
                } else if (!isKeyboardDefault) {
                    Text(
                        text = "Set AI Keyboard as default to use it automatically",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        // Enable Keyboard Button
        if (!isKeyboardEnabled) {
            Button(
                onClick = {
                    KeyboardUtils.openInputMethodSettings(context)
                    checkStatus = !checkStatus // Trigger status check
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Settings, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Enable AI Keyboard")
            }
            
            OutlinedButton(
                onClick = {
                    KeyboardUtils.showInputMethodPicker(context)
                    checkStatus = !checkStatus
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Keyboard, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Show Keyboard Picker")
            }
        } else if (!isKeyboardDefault) {
            Button(
                onClick = {
                    KeyboardUtils.openInputMethodSettings(context)
                    checkStatus = !checkStatus
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Star, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Set as Default Keyboard")
            }
        }

        Divider()

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

