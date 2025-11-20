package com.aikeyboard.ui.settings.sections

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun PrivacyPolicySection() {
    val scrollState = rememberScrollState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Privacy Policy",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        
        Text(
            text = "Last Updated: November 2024",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Divider()
        
        // Introduction
        Text(
            text = "Introduction",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "AI Keyboard is committed to protecting your privacy. This Privacy Policy explains how we handle your data when you use our keyboard application.",
            style = MaterialTheme.typography.bodyMedium
        )
        
        // Data Collection
        Text(
            text = "Data Collection",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "AI Keyboard operates with a privacy-first approach:",
            style = MaterialTheme.typography.bodyMedium
        )
        Column(
            modifier = Modifier.padding(start = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "• No personal data is collected or transmitted",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "• No analytics or tracking is performed",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "• All voice recognition is processed 100% offline on your device",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "• No audio data is sent to external servers",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        
        // Permissions
        Text(
            text = "Permissions",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "AI Keyboard requires the following permissions:",
            style = MaterialTheme.typography.bodyMedium
        )
        Column(
            modifier = Modifier.padding(start = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "• RECORD_AUDIO: Required for voice input functionality. Audio is only recorded when you actively use the voice input feature and is processed entirely on your device.",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "• INTERNET: Only used when you explicitly choose to download AI models from a URL. No network activity occurs without your explicit action.",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "• ACCESS_NETWORK_STATE: Used to check network availability before model downloads.",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "• READ_EXTERNAL_STORAGE / READ_MEDIA_AUDIO: Used only when you choose to install models from local files.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        
        // Local Data Storage
        Text(
            text = "Local Data Storage",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "The following data is stored locally on your device:",
            style = MaterialTheme.typography.bodyMedium
        )
        Column(
            modifier = Modifier.padding(start = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "• Personal dictionary: Words you add are stored in a local Room database",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "• Clipboard history: Managed locally, never transmitted",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "• Keyboard settings and preferences: Stored locally using DataStore",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "• AI models: Downloaded models are stored in app-private storage",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        
        // Third-Party Services
        Text(
            text = "Third-Party Services",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "AI Keyboard uses the following open-source libraries:",
            style = MaterialTheme.typography.bodyMedium
        )
        Column(
            modifier = Modifier.padding(start = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "• ONNX Runtime (Apache-2.0): For AI model inference",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "• Vosk (Apache-2.0): Alternative speech recognition engine",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "• Google Play Billing: For in-app purchases (if applicable)",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Text(
            text = "None of these services collect or transmit your personal data.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
        
        // Children's Privacy
        Text(
            text = "Children's Privacy",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "AI Keyboard does not knowingly collect any information from children under 13. Since we do not collect any data, this is not applicable.",
            style = MaterialTheme.typography.bodyMedium
        )
        
        // Changes to Privacy Policy
        Text(
            text = "Changes to This Privacy Policy",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "We may update this Privacy Policy from time to time. We will notify you of any changes by posting the new Privacy Policy in the app and updating the \"Last Updated\" date.",
            style = MaterialTheme.typography.bodyMedium
        )
        
        // Contact
        Text(
            text = "Contact Us",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "If you have any questions about this Privacy Policy, please contact us through the app's About section or visit our support page.",
            style = MaterialTheme.typography.bodyMedium
        )
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}

