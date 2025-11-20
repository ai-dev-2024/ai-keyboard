package com.aikeyboard.ui.settings.sections

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aikeyboard.clipboard.data.ClipboardDao
import kotlinx.coroutines.flow.collectAsState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@Composable
fun ClipboardSettingsSection(
    clipboardDao: ClipboardDao? = null
) {
    val scope = rememberCoroutineScope()
    val recentItems = remember { mutableStateOf<List<com.aikeyboard.clipboard.data.ClipboardEntry>>(emptyList()) }

    LaunchedEffect(Unit) {
        clipboardDao?.getRecent(50)?.collect { items ->
            recentItems.value = items
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Clipboard Manager",
            style = MaterialTheme.typography.headlineSmall
        )

        Divider()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Clipboard History")
            TextButton(onClick = {
                scope.launch {
                    clipboardDao?.clearAll()
                }
            }) {
                Text("Clear All")
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(recentItems.value) { item ->
                ClipboardItem(
                    item = item,
                    onPin = {
                        scope.launch {
                            clipboardDao?.update(item.copy(isPinned = !item.isPinned))
                        }
                    },
                    onDelete = {
                        scope.launch {
                            clipboardDao?.delete(item)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ClipboardItem(
    item: com.aikeyboard.clipboard.data.ClipboardEntry,
    onPin: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.text.take(100),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = item.timestamp.toString(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Row {
                IconButton(onClick = onPin) {
                    Icon(
                        if (item.isPinned) Icons.Default.PushPin else Icons.Default.PushPin,
                        "Pin"
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, "Delete")
                }
            }
        }
    }
}

