package com.aikeyboard.ui.settings.sections

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ModelInstallDialog(
    onDismiss: () -> Unit,
    onInstallFromFile: () -> Unit,
    onDownloadFromUrl: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Install Model") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Choose how to install the model:")
                Text(
                    text = "• From File: Select a model directory containing manifest.json and model files\n• From URL: Download a model from a URL",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        confirmButton = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextButton(onClick = {
                    onInstallFromFile()
                }) {
                    Text("From File")
                }
                TextButton(onClick = {
                    onDownloadFromUrl()
                }) {
                    Text("From URL")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun ModelUrlDialog(
    onDismiss: () -> Unit,
    onDownload: (String) -> Unit
) {
    var urlText by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Download Model from URL") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Enter the URL of the model to download:")
                OutlinedTextField(
                    value = urlText,
                    onValueChange = { urlText = it },
                    label = { Text("Model URL") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (urlText.isNotBlank()) {
                        onDownload(urlText)
                    }
                },
                enabled = urlText.isNotBlank()
            ) {
                Text("Download")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}









