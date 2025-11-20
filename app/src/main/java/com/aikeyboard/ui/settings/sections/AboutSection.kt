package com.aikeyboard.ui.settings.sections

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.browser.customtabs.CustomTabsIntent
import androidx.hilt.navigation.compose.hiltViewModel
import com.aikeyboard.ui.settings.viewmodel.SettingsViewModel
import com.aikeyboard.voiceinput.modelmanager.ModelLicenseRegistry
import kotlinx.coroutines.flow.collectAsState

@Composable
fun AboutSection(
    viewModel: SettingsViewModel = hiltViewModel(),
    onPrivacyPolicyClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val installedModels by viewModel.installedModels.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "AI Keyboard",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Version 1.0.0",
            style = MaterialTheme.typography.bodyLarge
        )

        Divider()

        Text(
            text = "A modern Android keyboard with offline AI speech-to-text",
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = "Copyright © 2024 AI Keyboard",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = "Licensed under Apache-2.0",
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Support the Developer Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Support the Developer ❤️",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Optional donation to support development",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Button(
                    onClick = {
                        val intent = CustomTabsIntent.Builder()
                            .setToolbarColor(MaterialTheme.colorScheme.primary.toArgb())
                            .build()
                            .intent
                            .apply {
                                data = Uri.parse("https://ko-fi.com/ai_dev_2024")
                            }
                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 2.dp
                    )
                ) {
                    Icon(
                        Icons.Default.Favorite,
                        "Support",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Support AI Keyboard ❤️",
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Text(
                    text = "Note: This is a voluntary donation and does not unlock any features. " +
                            "For premium features, please use the in-app purchase.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Privacy Notice",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "All voice recognition is processed 100% offline on your device. " +
                    "No audio data is sent to external servers. Your privacy is our priority.",
            style = MaterialTheme.typography.bodySmall
        )

        TextButton(
            onClick = onPrivacyPolicyClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Info, "Privacy Policy")
            Spacer(modifier = Modifier.width(8.dp))
            Text("View Full Privacy Policy")
        }

        Divider()

        // Model Licensing Section
        Text(
            text = "Model Licensing",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "AI Keyboard processes all voice input offline using ASR (Automatic Speech Recognition) models installed on your device. " +
                    "No models are bundled with the app to ensure legal compliance and keep the app size small.",
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Disclaimer for user-imported models
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = "Warning",
                        tint = MaterialTheme.colorScheme.error
                    )
                    Text(
                        text = "Important Disclaimer",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "Users are responsible for verifying the licensing terms of any custom models they install. " +
                            "The app does not bundle any model files and cannot guarantee the licensing compliance of user-imported models.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Installed Models with Licenses
        if (installedModels.isNotEmpty()) {
            Text(
                text = "Installed Models & Licenses",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            installedModels.forEach { model ->
                ModelLicenseCard(model = model)
            }
        } else {
            Text(
                text = "No models installed. Install a model in Voice Input settings to use offline speech recognition.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun ModelLicenseCard(model: com.aikeyboard.voiceinput.modelmanager.InstalledModel) {
    val context = LocalContext.current
    val license = model.manifest.license_type?.let { licenseType ->
        ModelLicenseRegistry.getLicense(model.id)?.copy(licenseType = licenseType)
    } ?: ModelLicenseRegistry.getLicense(model.id) 
        ?: ModelLicenseRegistry.getUnknownLicense()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = model.displayName,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "License: ${license.licenseType}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    if (license.copyrightHolder != null) {
                        Text(
                            text = "Copyright: ${license.copyrightHolder}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Text(
                        text = "Commercial Use: ${if (license.commercialUseAllowed) "✅ Allowed" else "❌ Not Allowed"}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "Redistribution: ${if (license.redistributionAllowed) "✅ Allowed" else "❌ Not Allowed"}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            if (license.attributionRequired) {
                Text(
                    text = "⚠️ Attribution Required",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            if (license.licenseUrl != null) {
                TextButton(
                    onClick = {
                        val intent = CustomTabsIntent.Builder()
                            .build()
                            .intent
                            .apply {
                                data = Uri.parse(license.licenseUrl)
                            }
                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("View License")
                }
            }

            if (license.licenseType == "Unknown") {
                Text(
                    text = "⚠️ License information not available. Please verify license compliance before commercial use.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

