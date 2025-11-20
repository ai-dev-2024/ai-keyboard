package com.aikeyboard.ui.settings.sections

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aikeyboard.ui.settings.viewmodel.SettingsViewModel

@Composable
fun VoiceInputSettingsSection(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val voiceInputEnabled by viewModel.voiceInputEnabled.collectAsState()
    val installedModels by viewModel.installedModels.collectAsState()
    val activeModel by viewModel.activeModel.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "AI Voice Input",
            style = MaterialTheme.typography.headlineSmall
        )

        Divider()

        SwitchPreference(
            title = "Enable Voice Input",
            checked = voiceInputEnabled,
            onCheckedChange = { viewModel.setVoiceInputEnabled(it) }
        )

        Divider()

        Text(
            text = "Installed Models",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 8.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(installedModels) { model ->
                ModelCard(
                    model = model,
                    isActive = model.id == activeModel,
                    onSetActive = { viewModel.setActiveModel(model.id) },
                    onDelete = { /* TODO */ }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { /* TODO: Open model installer */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Add, "Install Model")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Install Model")
        }
    }
}

@Composable
fun ModelCard(
    model: com.aikeyboard.voiceinput.modelmanager.InstalledModel,
    isActive: Boolean,
    onSetActive: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = if (isActive) {
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        } else {
            CardDefaults.cardColors()
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = model.displayName,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Engine: ${model.engine.uppercase()}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "Size: ${model.manifest.size_bytes / 1_000_000} MB",
                        style = MaterialTheme.typography.bodySmall
                    )
                    if (model.manifest.languages.isNotEmpty()) {
                        Text(
                            text = "Languages: ${model.manifest.languages.joinToString(", ")}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Row {
                    if (!isActive) {
                        Button(onClick = onSetActive) {
                            Text("Set Active")
                        }
                    } else {
                        Text(
                            text = "Active",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, "Delete")
                    }
                }
            }
        }
    }
}

