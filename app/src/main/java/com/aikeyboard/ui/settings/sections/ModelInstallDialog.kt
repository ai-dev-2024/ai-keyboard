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
            }
        },
        confirmButton = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextButton(onClick = {
                    onInstallFromFile()
                    onDismiss()
                }) {
                    Text("From File")
                }
                TextButton(onClick = {
                    onDownloadFromUrl()
                    onDismiss()
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

