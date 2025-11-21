package com.aikeyboard.ui.settings.sections

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aikeyboard.ui.settings.viewmodel.SettingsViewModel
import com.aikeyboard.voiceinput.modelmanager.ModelManager
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@Composable
fun VoiceInputSettingsSection(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var showInstallDialog by remember { mutableStateOf(false) }
    var showUrlDialog by remember { mutableStateOf(false) }
    var installError by remember { mutableStateOf<String?>(null) }
    var installSuccess by remember { mutableStateOf(false) }
    
    val voiceInputEnabled by viewModel.voiceInputEnabled.collectAsState()
    val installedModels by viewModel.installedModels.collectAsState()
    val activeModel by viewModel.activeModel.collectAsState()
    
    // File picker launcher for model directory
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                scope.launch {
                    try {
                        installError = null
                        installSuccess = false
                        // Copy model from URI to app's model directory
                        context.contentResolver.openInputStream(uri)?.use { input ->
                            // For now, we'll need the user to select a directory
                            // This is a simplified version - in production, use SAF properly
                            installError = "Model installation from file picker needs directory selection. Please use the model directory picker."
                        }
                    } catch (e: Exception) {
                        installError = "Failed to install model: ${e.message}"
                    }
                }
            }
        }
    }

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
            onClick = { showInstallDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Add, "Install Model")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Install Model")
        }
        
        if (installError != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = installError!!,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
        
        if (installSuccess) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Text(
                    text = "Model installed successfully!",
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
    
    // Model Install Dialog
    if (showInstallDialog) {
        ModelInstallDialog(
            onDismiss = { showInstallDialog = false },
            onInstallFromFile = {
                // Open directory picker for model installation
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                filePickerLauncher.launch(intent)
            },
            onDownloadFromUrl = {
                showInstallDialog = false
                showUrlDialog = true
            }
        )
    }
    
    // URL Download Dialog
    if (showUrlDialog) {
        ModelUrlDialog(
            onDismiss = { showUrlDialog = false },
            onDownload = { url ->
                scope.launch {
                    try {
                        installError = null
                        installSuccess = false
                        
                        if (url.isBlank()) {
                            installError = "Please enter a valid URL"
                            showUrlDialog = false
                            return@launch
                        }
                        
                        val result = viewModel.downloadModelFromUrl(url)
                        if (result.isSuccess) {
                            installSuccess = true
                            installError = null
                            showUrlDialog = false
                            // Show success message for a few seconds
                            kotlinx.coroutines.delay(3000)
                            installSuccess = false
                        } else {
                            installError = "Failed to start download. Please check the URL and try again."
                            showUrlDialog = false
                        }
                    } catch (e: Exception) {
                        installError = "Failed to download model: ${e.message}"
                        showUrlDialog = false
                    }
                }
            }
        )
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









