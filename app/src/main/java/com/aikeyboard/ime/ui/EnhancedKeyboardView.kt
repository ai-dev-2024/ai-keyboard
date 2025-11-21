package com.aikeyboard.ime.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aikeyboard.ime.autocorrect.AutoCorrectEngine
import com.aikeyboard.ime.emoji.EmojiPicker
import com.aikeyboard.ime.viewmodel.KeyboardViewModel
import com.aikeyboard.voiceinput.service.VoiceInputService
import kotlinx.coroutines.launch

@Composable
fun EnhancedKeyboardView(
    onTextInput: (String) -> Unit,
    onBackspace: () -> Unit,
    onEnter: () -> Unit,
    viewModel: KeyboardViewModel = hiltViewModel()
) {
    var showEmojiPicker by remember { mutableStateOf(false) }
    var currentText by remember { mutableStateOf("") }
    
    // Get ViewModel state
    val suggestions by viewModel.suggestions.collectAsState()
    val isVoiceRecording by viewModel.isVoiceRecording.collectAsState()
    val partialTranscription by viewModel.partialTranscription.collectAsState()
    val keycapShape by viewModel.keycapShape.collectAsState()
    
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (showEmojiPicker) {
            EmojiPicker(
                onEmojiSelected = { emoji ->
                    onTextInput(emoji)
                    showEmojiPicker = false
                },
                modifier = Modifier.height(200.dp)
            )
        } else {
            // Voice waveform when recording
            if (isVoiceRecording) {
                com.aikeyboard.ime.ui.VoiceWaveform(
                    isRecording = isVoiceRecording,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            
            // Suggestions bar with voice transcription
            SuggestionsBar(
                suggestions = if (partialTranscription != null) {
                    listOf<String>(partialTranscription!!) + suggestions
                } else {
                    suggestions
                },
                onSuggestionClick = { suggestion ->
                    onTextInput(suggestion)
                    currentText = ""
                    viewModel.updateCurrentText("")
                }
            )

            // Keyboard layout
            KeyboardLayout(
                onTextInput = { char ->
                    val newText = currentText + char
                    currentText = newText
                    viewModel.updateCurrentText(newText)
                    onTextInput(char)
                },
                onBackspace = {
                    currentText = currentText.dropLast(1)
                    viewModel.updateCurrentText(currentText)
                    onBackspace()
                },
                onEnter = onEnter,
                onEmojiClick = { showEmojiPicker = true },
                onVoiceClick = {
                    scope.launch {
                        if (isVoiceRecording) {
                            viewModel.stopVoiceInput()
                        } else {
                            viewModel.startVoiceInput()
                        }
                    }
                },
                isVoiceRecording = isVoiceRecording,
                keycapShape = keycapShape
            )
        }
    }
}

@Composable
fun KeyboardLayout(
    onTextInput: (String) -> Unit,
    onBackspace: () -> Unit,
    onEnter: () -> Unit,
    onEmojiClick: () -> Unit,
    onVoiceClick: () -> Unit,
    isVoiceRecording: Boolean,
    keycapShape: com.aikeyboard.ui.settings.sections.KeycapShape = com.aikeyboard.ui.settings.sections.KeycapShape.ROUNDED
) {
    val shape = androidx.compose.foundation.shape.RoundedCornerShape(keycapShape.cornerRadius.dp)
    val keys = listOf(
        listOf("Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"),
        listOf("A", "S", "D", "F", "G", "H", "J", "K", "L"),
        listOf("Z", "X", "C", "V", "B", "N", "M")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        keys.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                row.forEach { key ->
                    KeyButton(
                        text = key,
                        modifier = Modifier.weight(1f),
                        onClick = { onTextInput(key.lowercase()) },
                        keycapShape = shape
                    )
                }
            }
        }

        // Bottom row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Emoji button
            KeyButton(
                text = "😀",
                modifier = Modifier.weight(1.5f),
                onClick = onEmojiClick,
                keycapShape = shape
            )

            // Voice input button - uses MagentaPulse when recording
            KeyButton(
                text = if (isVoiceRecording) "⏹" else "🎤",
                modifier = Modifier.weight(1.5f),
                onClick = onVoiceClick,
                isHighlighted = isVoiceRecording,
                keycapShape = shape,
                highlightColor = if (isVoiceRecording) {
                    com.aikeyboard.ui.theme.MagentaPulse
                } else {
                    null
                }
            )

            // Space bar
            KeyButton(
                text = "Space",
                modifier = Modifier.weight(4f),
                onClick = { onTextInput(" ") },
                keycapShape = shape
            )

            // Backspace
            KeyButton(
                text = "⌫",
                modifier = Modifier.weight(1.5f),
                onClick = onBackspace,
                keycapShape = shape
            )

            // Enter
            KeyButton(
                text = "↵",
                modifier = Modifier.weight(1.5f),
                onClick = onEnter,
                keycapShape = shape
            )
        }
    }
}

@Composable
fun KeyButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isHighlighted: Boolean = false,
    keycapShape: androidx.compose.foundation.shape.RoundedCornerShape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
    highlightColor: androidx.compose.ui.graphics.Color? = null
) {
    val backgroundColor = if (isHighlighted) {
        highlightColor ?: MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }
    
    val contentColor = if (isHighlighted) {
        if (highlightColor != null) {
            com.aikeyboard.ui.theme.PureWhite
        } else {
            MaterialTheme.colorScheme.onPrimaryContainer
        }
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }
    
    Surface(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        shape = keycapShape,
        color = backgroundColor,
        contentColor = contentColor,
        tonalElevation = if (isHighlighted) 2.dp else 0.dp
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = androidx.compose.ui.Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
            )
        }
    }
}

@Composable
fun SuggestionsBar(
    suggestions: List<String>,
    onSuggestionClick: (String) -> Unit
) {
    if (suggestions.isEmpty()) return
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        suggestions.take(5).forEach { suggestion ->
            SuggestionChip(
                text = suggestion,
                onClick = { onSuggestionClick(suggestion) }
            )
        }
    }
}

@Composable
fun SuggestionChip(
    text: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

