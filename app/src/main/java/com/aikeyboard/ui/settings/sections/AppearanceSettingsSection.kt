package com.aikeyboard.ui.settings.sections

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aikeyboard.ui.settings.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch

@Composable
fun AppearanceSettingsSection(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val themePreset by viewModel.themePreset.collectAsState()
    val isDarkTheme by viewModel.isDarkTheme.collectAsState()
    val keyboardHeight by viewModel.keyboardHeight.collectAsState()
    val keycapShape by viewModel.keycapShape.collectAsState()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "Appearance & Themes",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
        )

        Divider()

        // Theme Preset Selector
        ThemeSelector(
            selectedTheme = themePreset,
            onThemeSelected = { preset ->
                scope.launch {
                    viewModel.setThemePreset(preset)
                }
            }
        )

        Divider()

        // Light/Dark Mode Toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Dark Mode",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Use dark theme",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Switch(
                checked = isDarkTheme,
                onCheckedChange = { enabled ->
                    scope.launch {
                        viewModel.setDarkTheme(enabled)
                    }
                }
            )
        }

        Divider()

        // Keycap Shape Selector
        KeycapShapeSelector(
            selectedShape = keycapShape,
            onShapeSelected = { shape ->
                scope.launch {
                    viewModel.setKeycapShape(shape)
                }
            }
        )

        Divider()

        // Keyboard Height
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Keyboard Height",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
            )
            
            val heightPercent = (keyboardHeight / 10f).coerceIn(30f, 100f)
            var height by remember { mutableStateOf(heightPercent / 100f) }
            
            Slider(
                value = height,
                onValueChange = { newHeight ->
                    height = newHeight
                    scope.launch {
                        viewModel.setKeyboardHeight((newHeight * 100).toInt())
                    }
                },
                valueRange = 0.3f..1.0f,
                steps = 6
            )
            
            Text(
                text = "Height: ${(height * 100).toInt()}%",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

