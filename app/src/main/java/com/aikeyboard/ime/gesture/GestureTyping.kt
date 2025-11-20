package com.aikeyboard.ime.gesture

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlin.math.sqrt

data class KeyPosition(
    val key: String,
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float
)

class GestureTypingEngine {
    fun findNearestKey(
        position: Offset,
        keys: List<KeyPosition>
    ): KeyPosition? {
        return keys.minByOrNull { key ->
            val centerX = key.x + key.width / 2
            val centerY = key.y + key.height / 2
            val dx = position.x - centerX
            val dy = position.y - centerY
            sqrt(dx * dx + dy * dy)
        }
    }

    fun buildWord(
        path: List<KeyPosition>
    ): String {
        return path.map { it.key.lowercase() }.joinToString("")
    }
}

@Composable
fun GestureTypingHandler(
    keys: List<KeyPosition>,
    onWordComplete: (String) -> Unit,
    content: @Composable (isTracking: Boolean, currentPath: List<KeyPosition>) -> Unit
) {
    var isTracking by remember { mutableStateOf(false) }
    var currentPath by remember { mutableStateOf<List<KeyPosition>>(emptyList()) }
    val engine = remember { GestureTypingEngine() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        isTracking = true
                        currentPath = emptyList()
                    },
                    onDrag = { change, _ ->
                        val nearest = engine.findNearestKey(change.position, keys)
                        if (nearest != null && currentPath.lastOrNull() != nearest) {
                            currentPath = currentPath + nearest
                        }
                    },
                    onDragEnd = {
                        isTracking = false
                        if (currentPath.isNotEmpty()) {
                            val word = engine.buildWord(currentPath)
                            onWordComplete(word)
                            currentPath = emptyList()
                        }
                    },
                    onDragCancel = {
                        isTracking = false
                        currentPath = emptyList()
                    }
                )
            }
    ) {
        content(isTracking, currentPath)
    }
}

