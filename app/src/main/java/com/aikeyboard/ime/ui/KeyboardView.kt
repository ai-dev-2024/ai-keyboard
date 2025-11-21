package com.aikeyboard.ime.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun KeyboardView(
    onTextInput: (String) -> Unit,
    onBackspace: () -> Unit,
    onEnter: () -> Unit,
    onVoiceInput: () -> Unit
) {
    val keys = listOf(
        listOf("Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"),
        listOf("A", "S", "D", "F", "G", "H", "J", "K", "L"),
        listOf("Z", "X", "C", "V", "B", "N", "M")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Suggestions bar
        SuggestionsBar(
            suggestions = listOf("hello", "world", "test"),
            onSuggestionClick = onTextInput
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Keyboard rows
        keys.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                row.forEach { key ->
                    KeyButton(
                        text = key,
                        onClick = { onTextInput(key.lowercase()) }
                    )
                }
            }
        }

        // Bottom row with special keys
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Voice input button
            KeyButton(
                text = "🎤",
                modifier = Modifier.weight(1.5f),
                onClick = onVoiceInput
            )
            
            // Space bar
            KeyButton(
                text = "Space",
                modifier = Modifier.weight(4f),
                onClick = { onTextInput(" ") }
            )
            
            // Backspace
            KeyButton(
                text = "⌫",
                modifier = Modifier.weight(1.5f),
                onClick = onBackspace
            )
            
            // Enter
            KeyButton(
                text = "↵",
                modifier = Modifier.weight(1.5f),
                onClick = onEnter
            )
        }
    }
}

@Composable
fun KeyButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}











